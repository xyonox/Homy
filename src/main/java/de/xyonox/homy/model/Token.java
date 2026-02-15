package de.xyonox.homy.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@DatabaseTable(tableName = "tokens")
@Data
@NoArgsConstructor
public class Token {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "hash_token", canBeNull = false, index = true, unique = true)
    private String hashToken;

    @DatabaseField(canBeNull = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private User user;

    public Token(String hashToken, User user) {
        this.hashToken = hashToken;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}