package me.shelves.backend

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.shelves.backend.utile.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestHeader
import java.io.StringReader
import javax.validation.constraints.NotNull

@Transactional
class BaseController<T> {
    companion object : Log() {}

    private var mapper: ObjectMapper = ObjectMapper()

    @Autowired
    lateinit var baseRepository: BaseRepository<T>

    fun getAll(@RequestHeader("ShelvesPaging") pagingDetails : String, @NotNull entity: T): ResponseEntity<Page<T>> {
        val pageRequest = getPageRequest(pagingDetails)

        return ResponseEntity(this.baseRepository.findAll(pageRequest!!), HttpStatus.OK)
    }


    private fun getPageRequest(pagingDetails: String): PageRequest? {
        var pageRequest: PageRequest? = null
        val map = getParameterMap(pagingDetails)
        try {
            if(map["page"] != null && map["size"] != null) {
                pageRequest = if (map["sortfield"] != null && map["sortdirection"] != null){
                    val direction: Sort.Direction = if (map["sortdirection"].toString() == "ASC")
                        Sort.Direction.ASC else Sort.Direction.DESC
                    PageRequest.of(map["page"].toString().toInt(), map["size"].toString().toInt(),
                            Sort.by(direction, map["sortfield"].toString()))
                } else {
                    PageRequest.of(Integer.parseInt(map["page"].toString()),
                            Integer.parseInt(map["size"].toString()))
                }

            }
        } catch (e: NumberFormatException) {
            log.error("Numberformat Exception :" + e.message)
        }

        return pageRequest
    }

    private fun getParameterMap(jsonMap: String?): MutableMap<String, Any> {
        var map: MutableMap<String, Any> = mutableMapOf()
        if (null != jsonMap) {
            try {
                val jsonNode = mapper.valueToTree<JsonNode>(jsonMap)
                map = jsonNode.toMap()
                return map
            } catch (e: Exception) {
                log.error("Error parsing Parametermap: $jsonMap")
            }
        }
        return map
    }

    private fun JsonNode.transform(fn: MutableMap<String, Any>.() -> Unit): JsonNode =
            toMap().also(fn).let { mapper.valueToTree(it) }

    private fun JsonNode.toMap(): MutableMap<String, Any> =
            (mapper.readValue(StringReader(toString())) as MutableMap<String, Any>)
}