package com.roberta.alugueltrajes.service;

import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Blob;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UploadArquivosService {

    private final Storage storage;
    private String diretorio = System.getProperty("user.dir") + "/credentials.json";

    public UploadArquivosService() throws IOException {
        storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new FileInputStream(diretorio)))
                .build()
                .getService();
    }

    public String inserir(MultipartFile file) throws IOException {

        String nomeImagem = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        BlobId blobId = BlobId.of("projetotccroberta", nomeImagem);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        byte[] fileBytes = file.getBytes();

        Blob blob = storage.create(blobInfo, fileBytes);
        String objetoUrl = "https://storage.googleapis.com/" + blob.getBucket() + "/" + blob.getName();

        return objetoUrl;
    }

    public void deletar(String objetoUrl) {
        String bucketName = "projetotccroberta";
        String objetoName = objetoUrl.replace("https://storage.googleapis.com/" + bucketName + "/", "");
        BlobId blobId = BlobId.of(bucketName, objetoName);
        storage.delete(blobId);
    }

}
