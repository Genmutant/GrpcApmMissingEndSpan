package apmTests;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class MainServer
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        final Server server = ServerBuilder.forPort(53597).addService(new GrpcTestServer()).build();
        server.start();
        server.awaitTermination();
    }
}
