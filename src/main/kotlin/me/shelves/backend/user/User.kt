package me.shelves.backend.user

import me.shelves.backend.BaseEntity
import me.shelves.backend.user.model.RoleType
import me.shelves.backend.user.model.Salutation
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.validation.constraints.Size

@Entity
data class User(var email: String,
                var firstName: String,
                var lastName: String,
                @ValidPassword
                @Size(min = 6, message = "Password size at least greater 6")
                var password: String,
                var salutation: Salutation = Salutation.UNBEKANNT): BaseEntity() {

    constructor(email: String, firstName: String, lastName: String, salutation: Salutation)
            : this(email, firstName, lastName, "", salutation)


    var expired: Boolean = false
    var locked: Boolean = false
    var enable: Boolean = false
    @Enumerated
    var roleType: RoleType = RoleType.Guest
}

