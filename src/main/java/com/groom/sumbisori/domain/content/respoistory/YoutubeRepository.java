package com.groom.sumbisori.domain.content.respoistory;

import com.groom.sumbisori.domain.content.entity.Youtube;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface YoutubeRepository extends JpaRepository<Youtube, Long> {
    @Query(value = "SELECT * FROM youtube ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Youtube> findRandomList(int count);
}