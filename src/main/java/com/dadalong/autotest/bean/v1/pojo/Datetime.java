package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;

public class Datetime {

    /**
     * "@TableField" ：完成字段自动填充
     * "@TableField(exist = false)" ：注明非数据库字段属性
     * "字段填充策略FieldFill"：DEFAULT默认不处理、INSERT插入填充字段、UPDATE更新填充字段、INSERT_UPDATE插入和更新填充字段
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date updatedAt;

    public Datetime() {
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Datetime)) {
            return false;
        } else {
            Datetime other = (Datetime)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$createdAt = this.getCreatedAt();
                Object other$createdAt = other.getCreatedAt();
                if (this$createdAt == null) {
                    if (other$createdAt != null) {
                        return false;
                    }
                } else if (!this$createdAt.equals(other$createdAt)) {
                    return false;
                }

                Object this$updatedAt = this.getUpdatedAt();
                Object other$updatedAt = other.getUpdatedAt();
                if (this$updatedAt == null) {
                    if (other$updatedAt != null) {
                        return false;
                    }
                } else if (!this$updatedAt.equals(other$updatedAt)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Datetime;
    }

    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        Object $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : $createdAt.hashCode());
        Object $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : $updatedAt.hashCode());
        return result;
    }

    public String toString() {
        return "Datetime(createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
    }
}