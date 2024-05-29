package com.klug;

import com.klug.application.grpc.service.ProductGrpcService;
import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.TlsServerCredentials;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class Tp3ApiGRpcSoapApplication {


    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Tp3ApiGRpcSoapApplication.class, args);

        // Caminhos para os arquivos de certificado e chave
        File certChainFile = new File("src/main/resources/certificates/server.crt");
        File privateKeyFile = new File("src/main/resources/certificates/server.pem");

        // Configurar o contexto SSL
        SslContext sslContext = GrpcSslContexts.forServer(certChainFile, privateKeyFile).build();

        // Construir o servidor gRPC com TLS
        Server server = NettyServerBuilder.forPort(9091)
                .sslContext(sslContext)
                .addService(new ProductGrpcService())
                .addService(ProtoReflectionService.newInstance())
                .build()
                .start();

        // Aguarde a terminação do servidor (opcional)
        server.awaitTermination();
    }

}
