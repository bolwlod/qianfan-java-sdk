# qianfan-java-sdk
调用百度千帆大模型平台的Java SDK
# 使用代码参考
执行代码前需要在先执行
<br>
export QIANFAN_AK=stijSju7FsfYbumZfB9tKGQs
export QIANFAN_SK=3BgrGcjS3CzUTf4pBdXvISs9GMXVct3u
```
public class Test {

        public static void main(String[] args) throws Exception {

                QianfanClient client = QianfanClient.builder()
                                .callTimeout(ofSeconds(60))
                                .connectTimeout(ofSeconds(60))
                                .readTimeout(ofSeconds(60))
                                .writeTimeout(ofSeconds(60))
                                .logRequests()
                                .logResponses()
                                .logStreamingResponses()
                                .build();

                // Sync request
                ChatCompletionRequest request = ChatCompletionRequest.builder()
                                .addUserMessage("who are you? what can you do?")
                                .build();

                System.out.println(client.chatCompletion(request).execute());

                CompletableFuture<String> future = new CompletableFuture<String>();
                client.chatCompletion("who are you? what can you do?")
                                .onResponse(future::complete)
                                .onError(future::completeExceptionally)
                                .execute();

                String response = future.get(30, SECONDS);
                System.out.println(response);

                // Stream request
                StringBuilder responseBuilder = new StringBuilder();
                CompletableFuture<String> future2 = new CompletableFuture<String>();

                client.chatCompletion("介绍一下关于月亮的传说")
                                .onPartialResponse(responseBuilder::append)
                                .onComplete(() -> future2.complete(responseBuilder.toString()))
                                .onError(future::completeExceptionally)
                                .execute();

                response = future2.get(30, SECONDS);
                System.out.println(response);

                client.shutdown();
        }
}
```
