package ru.georgdeveloper.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.georgdeveloper.myapp.domain.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {}
