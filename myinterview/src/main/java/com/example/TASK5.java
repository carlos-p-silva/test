package com.example;

/**
 * Create an implementation of a Rest API .
 * Expose an API. Feel free to explore possibilities/functionalities/capabilities following Rest standard.
 * We suggest that your implementation have at least a CRUD scenario.
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.*;

public class TASK5 {

    private static Map<Long, Produto> produtos = new HashMap<>();
    private static long proximoId = 1;

    static class Produto {
        long id;
        String nome;
        double preco;

        Produto(long id, String nome, double preco) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
        }
    }

    static class ProdutoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                handleGet(exchange);
            } else if (requestMethod.equalsIgnoreCase("POST")) {
                handlePost(exchange);
            } else if (requestMethod.equalsIgnoreCase("PUT")) {
                handlePut(exchange);
            } else if (requestMethod.equalsIgnoreCase("DELETE")) {
                handleDelete(exchange);
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            List<Produto> result = new ArrayList<>(produtos.values());
            String response = new ObjectMapper().writeValueAsString(result);
            sendResponse(exchange, response, HttpURLConnection.HTTP_OK);
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                requestBody.append(line);
            }
            br.close();

            ObjectMapper objectMapper = new ObjectMapper();
            Produto newProduto = objectMapper.readValue(requestBody.toString(), Produto.class);
            newProduto.id = proximoId++;
            produtos.put(newProduto.id, newProduto);

            sendResponse(exchange, "", HttpURLConnection.HTTP_CREATED);
        }

        private void handlePut(HttpExchange exchange) throws IOException {
            long id = Long.parseLong(exchange.getRequestURI().getPath().split("/")[3]);
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                requestBody.append(line);
            }
            br.close();

            ObjectMapper objectMapper = new ObjectMapper();
            Produto updatedProduto = objectMapper.readValue(requestBody.toString(), Produto.class);
            updatedProduto.id = id;
            produtos.put(id, updatedProduto);

            sendResponse(exchange, "", HttpURLConnection.HTTP_OK);
        }

        private void handleDelete(HttpExchange exchange) throws IOException {
            long id = Long.parseLong(exchange.getRequestURI().getPath().split("/")[3]);
            produtos.remove(id);

            sendResponse(exchange, "", HttpURLConnection.HTTP_NO_CONTENT);
        }

        private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/api/produtos", new ProdutoHandler());
        server.setExecutor(null);
        server.start();
    }


}