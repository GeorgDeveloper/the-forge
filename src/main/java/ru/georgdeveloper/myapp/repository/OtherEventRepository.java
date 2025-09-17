package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.OtherEvent;

@Repository
public interface OtherEventRepository extends JpaRepository<OtherEvent, Long> {}
