package controllers;

import java.util.List;

import play.libs.Json;
import play.mvc.*;
import play.data.*;

import models.*;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class API extends Controller {
	// # Create node
	// POST /api/nodes
	public static Result addNode() {
		JsonNode json = request().body().asJson();
		ObjectNode result = Json.newObject();
		System.out.println("Inserindo node: " + json);

		if (json == null) {
			result.put("status", "ERR");
			result.put("message", "Node not added: Expecting Json data");
			return badRequest(result);
		} else {
			try {
				Node node = Json.fromJson(json, Node.class);
				System.out.println("Salvando node: " + Json.toJson(node));
				node.save();
				result.put("status", "OK");
				result.put("message", "Node added: " + Json.toJson(node));
				return ok(result);
			} catch (Exception e) {
				result.put("status", "ERR");
				result.put("message", "Node not added: " + e);
				return badRequest(result);
			}
		}
	}

	// # Retrieve node
	// GET /api/nodes/:uuid_name
	public static Result getNode(String uuid_name) {
		System.out.println("Pesquisando node por uuid ou name: " + uuid_name);

		List<Node> uuidlist = Node.find.where()
				.ieq("uuid", uuid_name)
				.findList();
		List<Node> namelist = Node.find.where()
				.ieq("name", uuid_name)
				.findList();
		if (!uuidlist.isEmpty()) {
			return ok(Json.toJson(uuidlist));
		} else if (!namelist.isEmpty()){
			return ok(Json.toJson(namelist));
		} else {
			ObjectNode result = Json.newObject();
			result.put("status", "ERR");
			result.put("message", "Node not found: " + uuid_name);
			return badRequest(result);
		}
    }

	// # Nodes list
	// GET /api/nodes
	public static Result lstNodes() {
    	return ok(Json.toJson(Node.find.all()));
    }

	// # Search nodes
	// GET /api/nodes/search/:uuid_name
	public static Result srchNodes(String uuid_name) {
		System.out.println("Pesquisando node por uuid ou name contendo: " + uuid_name);

		List<Node> uuidlist = Node.find.where()
				.ilike("uuid", "%" + uuid_name + "%")
				.findList();
		List<Node> namelist = Node.find.where()
				.ilike("name", "%" + uuid_name + "%")
				.findList();
		if (!uuidlist.isEmpty()) {
			return ok(Json.toJson(uuidlist));
		} else if (!namelist.isEmpty()){
			return ok(Json.toJson(namelist));
		} else {
			ObjectNode result = Json.newObject();
			result.put("status", "ERR");
			result.put("message", "No node was found with substring: " + uuid_name);
			return badRequest(result);
		}
    }

	// # Update node
	// PUT /api/nodes/:uuid_namee
	public static Result updNode(String uuid_name) {
		JsonNode json = request().body().asJson();
		ObjectNode result = Json.newObject();
		System.out.println("Atualizando node: " + uuid_name + " com o conteudo: " + json);

		if (json == null) {
			result.put("status", "ERR");
			result.put("message", "Node not updated: Expecting Json data");
			return badRequest(result);
		} else {
			try {
				Node uuidnode = Node.find.where()
						.ieq("uuid", uuid_name)
						.findUnique();
				if (uuidnode != null) {
					Node node = Json.fromJson(json, Node.class);
					uuidnode.uuid = node.uuid;
					uuidnode.name = node.name;
					uuidnode.info = node.info;
					System.out.println("Atualizando node: " + Json.toJson(node));
					uuidnode.save();
					result.put("status", "OK");
					result.put("message", "Node updated by uuid \"" + uuid_name + "\" with content: " + Json.toJson(node));
					return ok(result);
				}
			} catch (Exception e) {
				System.out.println("ERR: " + e);
			}
			
			try {
				Node namenode = Node.find.where()
						.ieq("name", uuid_name)
						.findUnique();
				if (namenode != null) {
					Node node = Json.fromJson(json, Node.class);
					namenode.uuid = node.uuid;
					namenode.name = node.name;
					namenode.info = node.info;
					System.out.println("Atualizando node: " + Json.toJson(node));
					namenode.save();
					result.put("status", "OK");
					result.put("message", "Node updated by name \"" + uuid_name + "\" with content: " + Json.toJson(node));
					return ok(result);
				}
			} catch (Exception e) {
				System.out.println("ERR: " + e);
			}
			
			result.put("status", "ERR");
			result.put("message", "Node not found: " + uuid_name);
			return badRequest(result);
		}
	}

	// # Delete node
	// DELETE /api/nodes/:uuid_name
	public static Result delNode(String uuid_name) {
		System.out.println("Deletando node por uuid ou name: " + uuid_name);
		ObjectNode result = Json.newObject();

		try {
			Node uuidnode = Node.find.where()
					.ieq("uuid", uuid_name)
					.findUnique();
			if (uuidnode != null) {
				uuidnode.delete();
				result.put("status", "OK");
				result.put("message", "Node deleted by uuid: " + uuid_name);
				return ok(result);
			}
		} catch (Exception e) {
			System.out.println("ERR: " + e);
		}
		
		try {
			Node namenode = Node.find.where()
					.ieq("name", uuid_name)
					.findUnique();
			if (namenode != null) {
				namenode.delete();
				result.put("status", "OK");
				result.put("message", "Node deleted by name: " + uuid_name);
				return ok(result);
			}
		} catch (Exception e) {
			System.out.println("ERR: " + e);
		}
		
		result.put("status", "ERR");
		result.put("message", "Node not found: " + uuid_name);
		return badRequest(result);
	}
}
