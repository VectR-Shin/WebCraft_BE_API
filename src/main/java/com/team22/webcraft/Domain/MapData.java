package com.team22.webcraft.Domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "map_data")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MapData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "map_data_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_data_id")
    private UserData userData;

    @Column(name = "map_name")
    private String mapName;

    @CreatedDate
    @Column(name = "create_time", updatable = false, columnDefinition = "datetime")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "last_update_time", columnDefinition = "datetime")
    private LocalDateTime lastUpdateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type")
    private AccessType accessType;

    // Constructor
    public MapData(String mapName, AccessType accessType) {
        this.mapName = mapName;
        this.accessType = accessType;
    }

    // userData setter
    public void setUserData_bothSide(UserData userData) {
        this.setUserData(userData);
        userData.addMapData(this);
    }

    // toString
    public String toString() {
        return "owner: " + this.userData.getUserId() + ", mapName: " + this.mapName + ", accessType: " + this.accessType.toString();
    }
}
