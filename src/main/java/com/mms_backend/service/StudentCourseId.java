package com.mms_backend.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentCourseId implements Serializable
{

    private String student_id;

    private String course_id;


    @Override
    public boolean equals(Object o)
    {
        if(this==o) return true;
        if(o==null || getClass() != o.getClass()) return false;

        StudentCourseId that= (StudentCourseId) o;
        return student_id.equals(that.student_id)&&course_id.equals(that.course_id);


    }



    @Override
    public int hashCode() {
        return Objects.hash(student_id,course_id);
    }
}
