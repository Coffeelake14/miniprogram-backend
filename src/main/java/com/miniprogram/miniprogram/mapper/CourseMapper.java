package com.miniprogram.miniprogram.mapper;

import com.miniprogram.miniprogram.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Select("SELECT * FROM course ORDER BY sort_order ASC, id DESC")
    List<Course> findAll();

    @Select("SELECT * FROM course WHERE id = #{id}")
    Course findById(Long id);

    @Select("SELECT * FROM course WHERE status = 'published' ORDER BY sort_order ASC, id DESC")
    List<Course> findPublished();

    @Select("SELECT * FROM course WHERE category_id = #{categoryId} AND status = 'published'")
    List<Course> findByCategory(Long categoryId);

    @Insert("INSERT INTO course(title, type, url, cover, category_id, duration, sort_order, status) " +
            "VALUES(#{title}, #{type}, #{url}, #{cover}, #{categoryId}, #{duration}, #{sortOrder}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);

    @Update("UPDATE course SET title=#{title}, type=#{type}, url=#{url}, cover=#{cover}, " +
            "category_id=#{categoryId}, duration=#{duration}, sort_order=#{sortOrder}, " +
            "status=#{status}, view_count=#{viewCount} WHERE id=#{id}")
    int update(Course course);

    @Delete("DELETE FROM course WHERE id = #{id}")
    int deleteById(Long id);

    @Update("UPDATE course SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(Long id);
}