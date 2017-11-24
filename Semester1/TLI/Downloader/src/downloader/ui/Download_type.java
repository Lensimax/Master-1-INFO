package downloader.ui;

import download.fc.Downloader;

public class Download_type {

    String url;
    Thread t;
    Downloader d;

    public Download_type(String url){
        this.url = url;
        this.d = new Downloader(url);
        this.t = new Thread(d);
        this.t.start();
    }

    public void resume(){
        this.d.resume();
    }

    public void pause(){
        this.d.pause();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Thread getT() {
        return t;
    }

    public void setT(Thread t) {
        this.t = t;
    }

    public Downloader getD() {
        return d;
    }

    public void setD(Downloader d) {
        this.d = d;
    }
}
