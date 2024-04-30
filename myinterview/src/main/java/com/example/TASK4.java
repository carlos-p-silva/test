package com.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


/**
 * Create an implementation of a Rest API client.
 * Prints out how many records exists for each gender and save this file to s3 bucket
 * API endpoint=> https://3ospphrepc.execute-api.us-west-2.amazonaws.com/prod/RDSLambda 
 * AWS s3 bucket => interview-digiage
 *
 */
public class TASK4 {

    public static void main(String[] args) {
        try {
            // URL do endpoint
            String endpointUrl = "https://3ospphrepc.execute-api.us-west-2.amazonaws.com/prod/RDSLambda";

            // Fazer uma solicitação HTTP ao endpoint
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Processar a resposta
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            // Contar registros por gênero
            Map<String, Integer> genreCounts = new HashMap<>();
            String jsonString = response.toString();
            int startIndex = jsonString.indexOf("[");
            int endIndex = jsonString.lastIndexOf("]");
            if (startIndex != -1 && endIndex != -1) {
                String recordsString = jsonString.substring(startIndex + 1, endIndex);
                String[] records = recordsString.split("\\},\\{");
                for (String record : records) {
                    String[] fields = record.split(",");
                    for (String field : fields) {
                        String[] keyValue = field.split(":");
                        if (keyValue.length == 2) {
                            String key = keyValue[0].trim().replaceAll("\"", "");
                            String value = keyValue[1].trim().replaceAll("\"", "");
                            if (key.equals("genre")) {
                                genreCounts.put(value, genreCounts.getOrDefault(value, 0) + 1);
                            }
                        }
                    }
                }
            }

            // Imprimir contagens de gênero
            for (Map.Entry<String, Integer> entry : genreCounts.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            // Configurar credenciais do AWS S3
            AwsBasicCredentials credentials = AwsBasicCredentials.create("******", "******");
            S3Client s3Client = S3Client.builder()
                    .region(Region.US_WEST_2)
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

            // Transformar contagens de gênero em formato de string
            StringBuilder countsString = new StringBuilder();
            for (Map.Entry<String, Integer> entry : genreCounts.entrySet()) {
                countsString.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            // Enviar contagens para o bucket S3
            String bucketName = "int-disaasage";
            String key = "genre_counts.txt";
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build(), RequestBody.fromString(countsString.toString()));

            System.out.println("Contagens de gênero enviadas com sucesso para o Amazon S3.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

