# ktor-authorization
![image](https://user-images.githubusercontent.com/29809879/206024735-fcab1133-d1e7-4004-92b8-4faa95bc85f0.png)

A Plugin to make authorization in ktor as easy as it gets

## How to use


##### install the plugin 
```bash
implementation("com.jaooooo:ktor-authorization:0.0.2")
```
##### Extend **RoleUser**
```bash
class MyUser(override val roles: Set<Role>, ....) : RoleUser()
```
Set your User Roles' by your preferred authentication method.

##### Enjoy authorizing with eash <3

Use one of the three pre-defined AuthType(s) or All Of them combined 

```WithAllRoles()``` 

Applies logical **AND** to the required roles, a user must have them all 

```WithAnyRole()``` 

Applies logical **OR** to the required roles, any role can do

```withoutRoles()```

Applies logical **NOT** to the required roles, a user can't have any of those

### Example
```bash
authenticate {
  withAllRoles("Moderator","Provider") {
    get("/secret-content") {
      call.respondText("I love ktor, tell nobody!")
    }
  }  
}
```
