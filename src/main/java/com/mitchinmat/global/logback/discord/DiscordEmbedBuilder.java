package com.mitchinmat.global.logback.discord;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.logback.discord.object.Author;
import com.mitchinmat.global.logback.discord.object.EmbedObject;
import com.mitchinmat.global.logback.discord.object.Field;
import com.mitchinmat.global.logback.discord.object.Footer;
import com.mitchinmat.global.logback.discord.object.Image;
import com.mitchinmat.global.logback.discord.object.JsonObject;
import com.mitchinmat.global.logback.discord.object.Thumbnail;

public class DiscordEmbedBuilder {

	public JsonObject createEmbedJson(List<EmbedObject> embeds) {
		if (embeds.isEmpty()) {
			throw new MitchinmatException(CREATE_DISCORD_APPEND_MESSAGE_FAILURE);
		}

		List<JsonObject> embedObjects = new ArrayList<>();

		for (EmbedObject embed : embeds) {
			JsonObject jsonEmbed = new JsonObject();

			jsonEmbed.put("title", embed.getTitle());
			jsonEmbed.put("description", embed.getDescription());
			jsonEmbed.put("url", embed.getUrl());

			processColor(embed, jsonEmbed);
			processFooter(embed.getFooter(), jsonEmbed);
			processImage(embed.getImage(), jsonEmbed);
			processThumbnail(embed.getThumbnail(), jsonEmbed);
			processAuthor(embed.getAuthor(), jsonEmbed);
			processFields(embed.getFields(), jsonEmbed);

			embedObjects.add(jsonEmbed);
		}

		JsonObject result = new JsonObject();
		result.put("embeds", embedObjects.toArray());
		return result;
	}

	private void processColor(EmbedObject embed, JsonObject jsonEmbed) {
		if (embed.getColor() != null) {
			Color color = embed.getColor();
			int rgb = color.getRed();
			rgb = (rgb << 8) + color.getGreen();
			rgb = (rgb << 8) + color.getBlue();

			jsonEmbed.put("color", rgb);
		}
	}

	private void processFooter(Footer footer, JsonObject jsonEmbed) {
		if (footer != null) {
			JsonObject jsonFooter = new JsonObject();
			jsonFooter.put("text", footer.getText());
			jsonFooter.put("icon_url", footer.getIconUrl());
			jsonEmbed.put("footer", jsonFooter);
		}
	}

	private void processImage(Image image, JsonObject jsonEmbed) {
		if (image != null) {
			JsonObject jsonImage = new JsonObject();
			jsonImage.put("url", image.getUrl());
			jsonEmbed.put("image", jsonImage);
		}
	}

	private void processThumbnail(Thumbnail thumbnail, JsonObject jsonEmbed) {
		if (thumbnail != null) {
			JsonObject jsonThumbnail = new JsonObject();
			jsonThumbnail.put("url", thumbnail.getUrl());
			jsonEmbed.put("thumbnail", jsonThumbnail);
		}
	}

	private void processAuthor(Author author, JsonObject jsonEmbed) {
		if (author != null) {
			JsonObject jsonAuthor = new JsonObject();
			jsonAuthor.put("name", author.getName());
			jsonAuthor.put("url", author.getUrl());
			jsonAuthor.put("icon_url", author.getIconUrl());
			jsonEmbed.put("author", jsonAuthor);
		}
	}

	private void processFields(List<Field> fields, JsonObject jsonEmbed) {
		List<JsonObject> jsonFields = new ArrayList<>();

		for (Field field : fields) {
			JsonObject jsonField = new JsonObject();

			jsonField.put("name", field.getName());
			jsonField.put("value", field.getValue());
			jsonField.put("inline", field.isInline());

			jsonFields.add(jsonField);
		}

		jsonEmbed.put("fields", jsonFields.toArray());
	}
}
