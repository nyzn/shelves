package me.shelves.backend.user.model

enum class RoleType(name: String) {
    Admin(Roles.ADMIN), Guest(Roles.GUEST), Free(Roles.FREE), Premium(Roles.PREMIUM);

    companion object {
        fun getRoleByName(name: String) = valueOf(name.toUpperCase())
    }


}