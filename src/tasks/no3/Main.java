package tasks.no3;


import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main {
    private static String url = "https://stackoverflow.com/questions/5120171/extract-links-from-a-web-page";
    public static void main(String[] args) throws IOException {
        printLinksFromUrl(url);
        printLinksWithCompletableFeature(url);

    }

    public static void printLinksFromUrl(String url) throws IOException {
        Elements links = Jsoup.connect(url).get().select("a[href]");
        links.forEach(
                (link) -> System.out.println(link.attr("abs:href"))
        );
    }
    public static void printLinksWithCompletableFeature(String url) throws IOException {
        CompletableFuture<Elements> future = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return Jsoup.connect(url).get().select("a[href]");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        future.thenAccept((links) -> links.forEach(
                (link) -> System.out.println(link.attr("abs:href"))
        ));
        ForkJoinPool.commonPool().awaitQuiescence(5, TimeUnit.SECONDS);
    }
}
