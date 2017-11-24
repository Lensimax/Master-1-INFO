package download.fc;

import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application {
	public void start(Stage stage) {
		for(String url: getParameters().getRaw()) {
			Downloader downloader;
			try {
				downloader = new Downloader(url);
			}
			catch(RuntimeException e) {
				System.err.format("skipping %s %s\n", url, e);
				continue;
			}
			System.out.format("Downloading %s:", downloader);
			
			downloader.progressProperty().addListener((obs, o, n) -> {
				System.out.print(".");
				System.out.flush();
			});
			
			String filename;
			try {
				filename = downloader.download();
			}
			catch(Exception e) {
				System.err.println("failed!");
				continue;
			}
			System.out.format("into %s\n", filename);
		}
		System.exit(0);
	}

	public static void main(String argv[]) {
		launch(argv);
	}
}
