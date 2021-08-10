package apmTests;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Scope;
import co.elastic.apm.api.Traced;
import co.elastic.apm.api.Transaction;
import co.elastic.apm.attach.ElasticApmAttacher;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import transaction.GrpcService;
import transaction.TransactionServiceGrpc;

import java.io.IOException;

public class MainClient
{
    public static void main(String[] args) throws InterruptedException
    {
        ElasticApmAttacher.attach();
        // Wait for setup, nicer log
        for (int i = 0; i < 1024; i++)
        {
            Thread.sleep(5);
        }
        final ManagedChannel channelServer = NettyChannelBuilder.forAddress("localhost", 53597).usePlaintext().build();
        final TransactionServiceGrpc.TransactionServiceBlockingStub blockingStubServer = TransactionServiceGrpc.newBlockingStub(channelServer);
        final Transaction transaction = ElasticApm.startTransaction();
        try(final Scope scope = transaction.activate()){
            transaction.setName("TestApmGrpc");
            //myTracedMethod();
            blockingStubServer.description(GrpcService.DescriptionRequest.newBuilder().build());
            //myOtherTracedMethod();
        }
        catch (Throwable t){
            transaction.captureException(t);
            throw t;
        }
        finally { transaction.end(); }
    }

    @Traced
    public static void myOtherTracedMethod() throws InterruptedException { Thread.sleep(5);}

    @Traced
    public static void myTracedMethod() throws InterruptedException { Thread.sleep(5);}

}
