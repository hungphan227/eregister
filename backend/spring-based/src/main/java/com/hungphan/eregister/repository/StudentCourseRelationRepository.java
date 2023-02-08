package com.hungphan.eregister.repository;

import com.hungphan.eregister.dto.CourseDto;
import com.hungphan.eregister.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hungphan.eregister.dto.CourseStatusDto;
import com.hungphan.eregister.model.StudentCourseRelation;

import java.util.List;

public interface StudentCourseRelationRepository extends JpaRepository<StudentCourseRelation, Long> {
    
    @Query("select count(sc.id) from StudentCourseRelation sc where sc.courseId = ?1 and sc.cancelled=false")
    int countNumberOfStudentInOneCourse(Long courseId);

    @Query("select new com.hungphan.eregister.dto.CourseStatusDto(courseId, count(id)) from StudentCourseRelation where cancelled=false group by courseId")
    List<CourseStatusDto> getCourseStatus();

    @Query("select new com.hungphan.eregister.dto.CourseStatusDto(sc.courseId, count(sc.id)) " +
            "from StudentCourseRelation sc join Course c on sc.courseId=c.id where c.courseNumber in ?1 and sc.cancelled=false group by sc.courseId")
    List<CourseStatusDto> countNumberOfStudentInCourses(List<String> courseNumbers);

    @Modifying
    @Query("update StudentCourseRelation set cancelled = true where courseId = ?1 and studentId = ?2")
    int updateCancelledByCourseIdAndStudentId(Long courseId, String studentId);
    
}
