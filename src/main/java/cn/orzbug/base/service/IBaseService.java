package cn.orzbug.base.service;


import cn.orzbug.base.entity.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: SQJ
 * @data: 2018/4/7 15:24
 * @version:
 */
@NoRepositoryBean
public interface IBaseService<T extends BaseEntity<I>, I extends Serializable> {

    T saveEntity(T t);

    T saveOrUpdate(T t);

    T getById(I id);

    void deleteById(I id);

    List<T> findAll();
}
