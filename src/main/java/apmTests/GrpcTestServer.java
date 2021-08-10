package apmTests;

import io.grpc.stub.StreamObserver;
import transaction.GrpcService;
import transaction.TransactionServiceGrpc;

public class GrpcTestServer extends TransactionServiceGrpc.TransactionServiceImplBase
{
    @Override
    public void description(final GrpcService.DescriptionRequest request, final StreamObserver<GrpcService.DescriptionResponse> responseObserver)
    {
        final GrpcService.DescriptionResponse response = GrpcService.DescriptionResponse.newBuilder().setMessage("Hello Grpc Client!").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
