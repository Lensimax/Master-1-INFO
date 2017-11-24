package download.fc;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import downloader.ui.Main;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;


public class Downloader extends Task {
	public static final int CHUNK_SIZE = 1024;

	URL url;
	int content_length;
	BufferedInputStream in;

	ReentrantLock rentrantlock;
	
	String filename;
	File temp;
	FileOutputStream out;


	int size = 0;
	int count = 0;
	
	public Downloader(String uri) {
		try {
            rentrantlock = new ReentrantLock();
			url = new URL(uri);
			
			URLConnection connection = url.openConnection();
			content_length = connection.getContentLength();
			
			in = new BufferedInputStream(connection.getInputStream());
			
			String[] path = url.getFile().split("/");
			filename = path[path.length-1];
			temp = File.createTempFile(filename, ".part");
			out = new FileOutputStream(temp);
		}
		catch(MalformedURLException e) { throw new RuntimeException(e); }
		catch(IOException e) { throw new RuntimeException(e); }
	}

	public String toString() {
		return url.toString();
	}

    @Override
    protected Object call() throws Exception {
        download();
        return null;
    }

//    public ReadOnlyDoubleProperty progressProperty() {
//		return progress.getReadOnlyProperty();
//	}
	
	protected String download() throws InterruptedException {
		byte buffer[] = new byte[CHUNK_SIZE];

        while(count >= 0) {

            rentrantlock.lock();

			try {
				out.write(buffer, 0, count);
			}
			catch(IOException e) { continue; }

            rentrantlock.unlock();

			size += count;
            updateProgress(1.*size/content_length, 1);
            Thread.sleep(1000);

            try {
                count = in.read(buffer, 0, CHUNK_SIZE);
            }
			catch(IOException e) { continue; }

        }

        if(size < content_length) {
            temp.delete();
			throw new InterruptedException();
		}
			
		temp.renameTo(new File(filename));
		return filename;
	}

	public void resume(){
        rentrantlock.unlock();
    }

	public void pause(){
        rentrantlock.lock();
    }

};
