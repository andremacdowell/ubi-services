package crublib;

import java.util.List;

import play.libs.Json;

import models.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CrudLib {
	// # Create node
	public static JsonNode addNode(JsonNode json) {
		ObjectNode result = Json.newObject();
		JsonNode result_json = Json.newObject();

		try {
			Node node = Json.fromJson(json, Node.class);
			System.out.println("[CrudLib] Salvando node: " + Json.toJson(node));
			node.save();
			result.put("status", "OK");
			result.put("message", "Node added: " + Json.toJson(node));
		} catch (Exception e) {
			result.put("status", "ERR");
			result.put("message", "Node not added: " + e);
		}

		result_json = Json.fromJson(result, JsonNode.class);
		return result_json;
	}

	// # Retrieve node
	public static JsonNode getNode(String uuid_name) {
		ObjectNode result = Json.newObject();
		JsonNode result_json = Json.newObject();
		System.out.println("[CrudLib] Pesquisando node por uuid ou name: "
				+ uuid_name);

		List<Node> uuidlist = Node.find.where().ieq("uuid", uuid_name)
				.findList();
		List<Node> namelist = Node.find.where().ieq("name", uuid_name)
				.findList();
		if (!uuidlist.isEmpty()) {
			System.out.println("[CrudLib] Encontrado node por uuid: "
					+ uuid_name);
			result_json = Json.toJson(uuidlist);
		} else if (!namelist.isEmpty()) {
			System.out.println("[CrudLib] Encontrado node por name: "
					+ uuid_name);
			result_json = Json.toJson(namelist);
		} else {
			result.put("status", "ERR");
			result.put("message", "Node not found: " + uuid_name);
			result_json = Json.fromJson(result, JsonNode.class);
		}

		return result_json;
	}

	// # Nodes list
	public static JsonNode lstNodes() {
		JsonNode result_json = Json.newObject();
		System.out.println("[CrudLib] Listando todos os nodes");

		result_json = Json.toJson(Node.find.all());
		return result_json;
	}

	// # Search nodes
	public static JsonNode srchNodes(String uuid_name) {
		ObjectNode result = Json.newObject();
		JsonNode result_json = Json.newObject();
		System.out
				.println("[CrudLib] Pesquisando node por uuid ou name contendo: "
						+ uuid_name);

		List<Node> uuidlist = Node.find.where()
				.ilike("uuid", "%" + uuid_name + "%").findList();
		List<Node> namelist = Node.find.where()
				.ilike("name", "%" + uuid_name + "%").findList();
		if (!uuidlist.isEmpty()) {
			System.out.println("[CrudLib] Encontrado node por uuid contendo: "
					+ uuid_name);
			result_json = Json.toJson(uuidlist);
		} else if (!namelist.isEmpty()) {
			System.out.println("[CrudLib] Encontrado node por name contendo: "
					+ uuid_name);
			result_json = Json.toJson(namelist);
		} else {
			result.put("status", "ERR");
			result.put("message", "No node was found with substring: "
					+ uuid_name);
			result_json = Json.fromJson(result, JsonNode.class);
		}

		return result_json;
	}

	// # Update node
	public static JsonNode updNode(String uuid_name, JsonNode json) {
		ObjectNode result = Json.newObject();
		JsonNode result_json = Json.newObject();
		System.out.println("[CrudLib] Atualizando node: " + uuid_name
				+ " com o conteudo: " + json);

		try {
			Node uuidnode = Node.find.where().ieq("uuid", uuid_name)
					.findUnique();
			if (uuidnode != null) {
				Node node = Json.fromJson(json, Node.class);
				uuidnode.uuid = node.uuid;
				uuidnode.name = node.name;
				uuidnode.info = node.info;
				System.out.println("[CrudLib] Atualizando node por uuid \""
						+ uuid_name + "\" com conteúdo: " + Json.toJson(node));
				uuidnode.save();
				result.put("status", "OK");
				result.put("message", "Node updated by uuid \"" + uuid_name
						+ "\" with content: " + Json.toJson(node));
				result_json = Json.fromJson(result, JsonNode.class);
				return result_json;
			}
		} catch (Exception e) {
			System.out.println("[CrudLib] ERR: " + e);
		}

		try {
			Node namenode = Node.find.where().ieq("name", uuid_name)
					.findUnique();
			if (namenode != null) {
				Node node = Json.fromJson(json, Node.class);
				namenode.uuid = node.uuid;
				namenode.name = node.name;
				namenode.info = node.info;
				System.out.println("[CrudLib] Atualizando node por name \""
						+ uuid_name + "\" com conteúdo: " + Json.toJson(node));
				namenode.save();
				result.put("status", "OK");
				result.put("message", "Node updated by name \"" + uuid_name
						+ "\" with content: " + Json.toJson(node));
				result_json = Json.fromJson(result, JsonNode.class);
				return result_json;
			}
		} catch (Exception e) {
			System.out.println("[CrudLib] ERR: " + e);
		}

		result.put("status", "ERR");
		result.put("message", "Node not found: " + uuid_name);
		result_json = Json.fromJson(result, JsonNode.class);
		return result_json;
	}

	// # Delete node
	public static JsonNode delNode(String uuid_name) {
		ObjectNode result = Json.newObject();
		JsonNode result_json = Json.newObject();
		System.out.println("[CrudLib] Deletando node por uuid ou name: "
				+ uuid_name);

		try {
			Node uuidnode = Node.find.where().ieq("uuid", uuid_name)
					.findUnique();
			if (uuidnode != null) {
				System.out.println("[CrudLib] Deletando node por uuid: "
						+ uuid_name);
				uuidnode.delete();
				result.put("status", "OK");
				result.put("message", "Node deleted by uuid: " + uuid_name);
				result_json = Json.fromJson(result, JsonNode.class);
				return result_json;
			}
		} catch (Exception e) {
			System.out.println("[CrudLib] ERR: " + e);
		}

		try {
			Node namenode = Node.find.where().ieq("name", uuid_name)
					.findUnique();
			if (namenode != null) {
				System.out.println("[CrudLib] Deletando node por name: "
						+ uuid_name);
				namenode.delete();
				result.put("status", "OK");
				result.put("message", "Node deleted by name: " + uuid_name);
				result_json = Json.fromJson(result, JsonNode.class);
				return result_json;
			}
		} catch (Exception e) {
			System.out.println("[CrudLib] ERR: " + e);
		}

		result.put("status", "ERR");
		result.put("message", "Node not found: " + uuid_name);
		result_json = Json.fromJson(result, JsonNode.class);
		return result_json;
	}
}
