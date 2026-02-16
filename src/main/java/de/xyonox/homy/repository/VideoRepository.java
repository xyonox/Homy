package de.xyonox.homy.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import de.xyonox.homy.config.DatabaseManager;
import de.xyonox.homy.model.Video;

import java.util.List;

public class VideoRepository {
    
    private final Dao<Video, Integer> dao;
    
    public VideoRepository() throws Exception {
        dao = DaoManager.createDao(
                DatabaseManager.getConnection(), 
                Video.class
        );
    }
    
    public void create(Video video) throws Exception {
        dao.create(video);
    }
    
    public Video findById(int id) throws Exception {
        return dao.queryForId(id);
    }
    
    public Video findByUrl(String url) throws Exception {
        return dao.queryBuilder()
                .where()
                .eq("url", url)
                .queryForFirst();
    }
    
    public Iterable<Video> findAll() throws Exception {
        return dao.queryForAll();
    }
    
    public List<Video> findByUserId(int userId) throws Exception {
        return dao.queryBuilder()
                .where()
                .eq("uploader_id", userId)
                .query();
    }

    public void delete(Video video) throws Exception {
        dao.delete(video);
    }
    
    public void update(Video video) throws Exception {
        dao.update(video);
    }
}
