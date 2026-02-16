package de.xyonox.homy.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DatabaseTable(tableName = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true, canBeNull = false)
    private String title;

    @DatabaseField(canBeNull = false)
    private String url;

    @DatabaseField(canBeNull = false)
    private String thumbnailUrl;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            columnName = "uploader_id"
    )
    private User uploader;

    @DatabaseField(canBeNull = false)
    private long createdAt;

    public Video(String title, String url, String thumbnailUrl, String description, User uploader) {
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.uploader = uploader;

        createdAt = System.currentTimeMillis();
    }
}