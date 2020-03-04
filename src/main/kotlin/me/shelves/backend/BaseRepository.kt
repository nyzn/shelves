package me.shelves.backend

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository

@NoRepositoryBean
interface BaseRepository<T> :
        PagingAndSortingRepository<T, Long>,
        JpaRepository<T, Long>,
        JpaSpecificationExecutor<T> {

    fun findByUuid(uuid: String): T
}