from fastapi import FastAPI, HTTPException, BackgroundTasks, status
from fastapi.responses import JSONResponse
from queue import Queue
from threading import Thread
from contextlib import asynccontextmanager
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import mysql.connector
from mysql.connector import Error
import random
import time
import re
import os
import logging
from tqdm import tqdm


"""
pip3 install fastapi uvicorn selenium mysql-connector-python webdriver-manager tqdm
"""


@asynccontextmanager
async def lifespan(app: FastAPI):
    start()
    yield
    shutdown()

app = FastAPI(lifespan=lifespan)

# 셀레니움 옵션
chrome_options = Options()
chrome_options.add_argument("--headless")
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("--disable-dev-shm-usage")

# logging 옵션
logger = logging.getLogger("file_logger")
logger.setLevel(logging.DEBUG)
file_handler = logging.FileHandler("crawler.log", mode="a")
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)
logger.addHandler(file_handler)

# driver 할당할 변수
options = webdriver.ChromeOptions()
options.add_argument("--headless")
options.add_argument("--disable-gpu")
options.add_argument("--no-sandbox")
options.add_argument("--disable-dev-shm-usage")
driver = webdriver.Chrome(service=Service(), options=chrome_options)

# DB config -> 일단 환경변수 세팅은 나중에
config = {
    'user': os.getenv('MITCHINMAT_MYSQL_USER'),
    'password': os.getenv('MITCHINMAT_MYSQL_PW'),
    'host': os.getenv('MITCHINMAT_MYSQL_HOST'),
    'database': os.getenv('MITCHINMAT_MYSQL_DB_NAME'),
    'raise_on_warnings': True
}

url_prefix = 'https://place.map.kakao.com/'
pattern = r'url\((.*?)\)'

"""
추가된 column들

bg_present -> image_url : 이미지 url
list_operation -> operation : list_operation 내부 내용들 $ 로 구분
location_detail -> tags : location_detail 내부 태그들 $ 로 구분

total_evalutaion -> total_kakao_reviews : span_class="color_b"안에 숫자파싱
num_rate -> kakao_review_score : float 파싱

"""


def crawling_all():
    global driver
    try:
        conn = mysql.connector.connect(**config)
        conn.autocommit = True
        cursor = conn.cursor()

        cursor.execute("SELECT id, image_url FROM place WHERE image_url is null")
        rows = cursor.fetchall()
        logger.info(f"크롤링 돌아갈 place 숫자 : {len(rows)}")

        for id, _ in tqdm(rows):
            try:
                update_query = get_update_query_by_id(id, driver)
                cursor.execute(update_query)
            except Error as err:
                logger.error(f"Error: {err}")
            except Exception as e:
                logger.error(f"Unexpected error: {e}")

        # 자원반환
        cursor.close()
        conn.close()

    except Exception as e:
        print(f"Unexpected error in function: {e}")
        raise HTTPException(status_code=500, detail="process failed.")


def crawl_by_id(place_id):
    global driver
    try:
        conn = mysql.connector.connect(**config)
        conn.autocommit = True
        cursor = conn.cursor()

        cursor.execute(f"SELECT id, place_url FROM place WHERE id = {place_id}")
        rows = cursor.fetchall()

        if rows:
            id, _ = rows[0]
            logger.info(f"place_id : {id}")
            try:
                update_query = get_update_query_by_id(id, driver)
                cursor.execute(update_query)
            except Error as err:
                logger.error(f"Error: {err}")
            except Exception as e:
                logger.error(f"Unexpected error: {e}")

        cursor.close()
        conn.close()

    except Exception as e:
        print(f"Unexpected error in function: {e}")
        raise HTTPException(status_code=500, detail="process failed.")


def get_update_query_by_id(id, driver):
    url = url_prefix + str(id)
    update_query = "UPDATE place SET "

    driver.get(url)
    time.sleep(random.uniform(1, 2))

    # image url 추가
    bg_elements = driver.find_elements(By.CLASS_NAME, "bg_present")
    image_url = "NA"
    if len(bg_elements) != 0:
        style_attribute = bg_elements[0].get_attribute("style")
        match = re.search(pattern, style_attribute)
        if match:
            image_url = match.group(1).strip('\'"')
            if image_url.startswith('//'):
                image_url = 'https:' + image_url
            if len(image_url) > 255:
                image_url = "NA"
    update_query += f"image_url = '{image_url}', "

    # operation time 추가
    oper_elements = driver.find_elements(By.CLASS_NAME, "list_operation")
    if len(oper_elements) == 0:
        oper_time = "NA"
    else:
        buttons = driver.find_elements(By.CSS_SELECTOR,
                                       "a.btn_more[data-logevent='main_info,more_timeinfo']")
        if buttons:
            button = WebDriverWait(driver, 10).until(
                EC.element_to_be_clickable(
                    (By.CSS_SELECTOR, "a.btn_more[data-logevent='main_info,more_timeinfo']"))
            )
            button.click()

            parent_element = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.CSS_SELECTOR, "div.inner_floor"))
            )
            list_operation = parent_element.find_element(By.CSS_SELECTOR, "ul.list_operation")

            list_items = list_operation.find_elements(By.TAG_NAME, "li")
            oper_time = ""
            for item in list_items:
                oper_time += item.text + "\n"

            button.click()
        else:
            oper_time = ""
            for item in oper_elements:
                oper_time += item.text

    if len(oper_time) > 255:
        oper_time = "NA"
    update_query += f"operation_time = '{oper_time}', "

    # tag 추가
    tag_elem = driver.find_elements(By.CLASS_NAME, "txt_tag")
    if len(tag_elem) == 0:
        tags = "NA"
    else:
        tags = ""
        for elem in tag_elem:
            tags += elem.text
        if len(tags) > 255:
            tags = "NA"
    update_query += f"tags = '{tags}', "

    # reveiw_count
    review_elem = driver.find_elements(By.CLASS_NAME, "total_evaluation")
    if len(review_elem) == 0:
        review = "0"
    else:
        review = review_elem[0].text.split(" ")[1]
    update_query += f"total_kakao_reviews = {review}, "

    score_elem = driver.find_elements(By.CLASS_NAME, "num_rate")
    if len(score_elem) == 0:
        score = "0.0"
    else:
        score = "0.0"
        for scores in score_elem:
            if scores.text.endswith("점"):
                score = scores.text.replace("점", "")
                break
    update_query += f"kakao_review_score = {score} "

    update_query += f"WHERE id = {id}"
    logger.info(update_query)
    return update_query


def start():
    global driver
    app.state.task_queue = Queue()
    app.state.worker_thread = Thread(target=process_queue, args=(app.state.task_queue,), daemon=True)
    app.state.worker_thread.start()
    if driver is None or not is_driver_alive():
        options = webdriver.ChromeOptions()
        options.add_argument("--headless")
        options.add_argument("--disable-gpu")
        options.add_argument("--no-sandbox")
        options.add_argument("--disable-dev-shm-usage")
        driver = webdriver.Chrome(service=Service(), options=chrome_options)


def shutdown():
    global driver
    app.state.task_queue.put(None)
    app.state.worker_thread.join()
    if driver:
        driver.quit()


def is_driver_alive():
    try:
        driver.current_url  # 드라이버가 정상 동작 중인지 확인
        return True
    except:
        return False


def process_queue(task_queue: Queue):
    """작업 큐를 처리하는 스레드 함수"""
    logger.info("queue 실행중")
    while True:
        task = task_queue.get()
        logger.info(f"task get : {task}")
        if task is None:  # 종료 신호
            break

        task_type, task_data = task
        try:
            if task_type == "crawl_all":
                crawling_all()
            elif task_type == "crawl_by_id":
                crawl_by_id(task_data)
        except Exception as e:
            logger.error(f"Error processing task: {e}")
        finally:
            app.state.task_queue.task_done()


@app.post("/crawling-all")
async def request_crawling_all():
    logger.info("crawling-all task에 추가")
    app.state.task_queue.put(("crawl_all", None))
    return JSONResponse(content={"message": "Task accepted"},status_code=status.HTTP_202_ACCEPTED)


@app.post("/crawling/{place_id}")
async def request_crawling_one(place_id):
    logger.info(f"cralwing {place_id} task에 추가")
    app.state.task_queue.put(("crawl_by_id", int(place_id)))
    return JSONResponse(content={"message": "Task accepted "+ str(place_id)},status_code=status.HTTP_202_ACCEPTED)


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("crawling_script:app", host="0.0.0.0", port=8000, reload=True)