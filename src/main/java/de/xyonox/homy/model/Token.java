package de.xyonox.homy.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@DatabaseTable(tableName = "tokens")
@Data
@NoArgsConstructor
public class Token {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "hash_token", canBeNull = false, unique = true)
    private String hashToken;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm", canBeNull = false)
    private Date createdAt = new Date();

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private User user;

    public Token(String hashToken, User user) {
        this.hashToken = hashToken;
        this.user = user;
        setCreatedAt(LocalDateTime.now());
    }

    public LocalDateTime getCreatedAtLocalDateTime() {
        return LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault());
    }

    public void setCreatedAt(LocalDateTime value) {
        if (value == null) throw new IllegalArgumentException("createdAt must not be null");
        this.createdAt = Date.from(value.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setCreatedAtNow() {
        setCreatedAt(LocalDateTime.now());
    }
}