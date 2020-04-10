package me.shelves.backend

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository

@NoRepositoryBean
interface BaseRepository<T> :
        PagingAndSortingRepository<T, Long>,
        CrudRepository<T,Long>,
        JpaRepository<T, Long>{

    fun findByUuid(uuid: String): T
}