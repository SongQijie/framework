package cn.orzbug.base.repository.impl;


import cn.orzbug.base.entity.BaseEntity;
import cn.orzbug.base.repository.IBaseRepository;
import cn.orzbug.base.specification.SpecificationUtils;
import cn.orzbug.base.utils.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * @author: SQJ
 * @data: 2018/12/6 13:16
 * @version:
 */
@Slf4j
@Transactional
public class BaseRepositoryImpl<T extends BaseEntity<I>, I extends Serializable> extends SimpleJpaRepository<T, I> implements IBaseRepository<T, I> {

    private final EntityManager entityManager;


    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }


    @Override
    public T saveOrUpdate(T t) {
        if (null == t.getId()) {
            return this.save(t);
        }
        return this.update(t);
    }

    @Override
    public T update(T t) {
        T oldT = get(t.getId());
        BeanUtils.copyProperties(t, oldT, ReflectionUtil.getNullPropertyNames(t, "id"));
        oldT = super.save(oldT);
        return oldT;
    }

    @Override
    public T get(I id) {
        return getOne(id);
    }


    @Override
    public Page<T> findAll(T t, Pageable pageable) {
        if (null == t) {
            return findAll(pageable);
        }
        Specification<T> specification = SpecificationUtils.getSpecificationForEntity(t);
        return findAll(specification, pageable);
    }

    @Override
    public List<T> findAll(T t) {
        Specification<T> specification = SpecificationUtils.getSpecificationForEntity(t);
        return findAll(specification);
    }

    @Override
    public Page<T> findAllEntities(Specification<T> specification, Pageable pageable) {
        return findAll(specification, pageable);
    }
}
