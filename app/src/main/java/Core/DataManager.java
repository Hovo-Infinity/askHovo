package Core;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataManager {

    private static DataManager dataManager;
    private List<String> imageUrls;
    private String imageUrl;

    public static DataManager getInstance(){

        if(dataManager == null){
            dataManager = new DataManager();
            dataManager.imageUrls = Arrays.asList(
                    "https://i.pinimg.com/originals/24/45/fb/2445fbdbebc12e5ffe412790a3229873.jpg",
                    "http://ezba.info/wp-content/uploads/2018/07/vintage-wallpapers-for-galaxy-lovely-kind-of-reminds-me-of-wel-e-to-night-vale-of-vintage-wallpapers-for-galaxy.jpg",
                    "https://www.wallpaperwolf.com/wallpapers/iphone-wallpapers/hd/download/night-stars-0467.png",
                    "https://spliffmobile.com/download/night-sky-7391.jpg",
                    "https://i.pinimg.com/originals/2e/89/e9/2e89e9fd55a0087abd5f302a248e60c4.jpg",
                    "https://i.pinimg.com/originals/ca/57/92/ca57925e9d03250f3e818d8022ce2a48.jpg",
                    "http://www.vactualpapers.com/web/images/April-28-2016/Manhattan%20at%20Night%20HD%20Mobile%20Wallpaper.jpg",
                    "https://wallpapershome.com/images/pages/pic_v/547.jpg");
            dataManager.imageUrl = dataManager.imageUrls.get(0);
        }
        return dataManager;
    }

    private DataManager(){}

    public List<String> getImageUrls(){
        return imageUrls;
    }

    public String getSelecredImage() {
        return imageUrl;
    }

    public void setSelectedIndex(int index) {
        imageUrl = imageUrls.get(index);
    }

    public void setRandomBackground() {
        Random rand = new Random();
        int max = imageUrls.size();
        int min = 0;
        int index = rand.nextInt((max - min)) + min;
        setSelectedIndex(index);
    }

}
