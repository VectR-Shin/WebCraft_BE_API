package com.team22.webcraft.Domain;

import com.team22.webcraft.SHA256.SHA256Generator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_data")
@Data
@NoArgsConstructor
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_data_id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MapData> mapDataList;

    @Column(name = "user_id", unique = true, length = 20)
    private String userId;

    @Column(name = "user_pw", length = 100)
    private String userPw;

    // Constructor
    public UserData(String userId, String originPw) {
        this.userId = userId;
        // sha256
        this.userPw = SHA256Generator.generateSHA256(originPw);
    }

    // mapData setter
    public void addMapData(MapData mapData) {
        if (this.mapDataList == null)
            this.mapDataList = new ArrayList<>();

        this.mapDataList.add(mapData);
    }

    public void addMapData_bothSide(MapData mapData) {
        this.addMapData(mapData);
        mapData.setUserData(this);
    }

    // toString
    public String toString() {
        return this.userId + this.userPw;
    }
}
