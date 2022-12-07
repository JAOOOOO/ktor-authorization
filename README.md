# ktor-authorization
![image](https://user-images.githubusercontent.com/29809879/206024735-fcab1133-d1e7-4004-92b8-4faa95bc85f0.png)

A Plugin to make authorization in ktor as easy as it gets

## How to use

Add the package to your build files

install the plugin ```implementation("com.jaooooo.ktor-authorization-0.0.1")```

Let your defined Principal extend of **RoleUser** instead of **Principal**

override roles of the user with user roles' when you set your principal

use one of the three defined interfaces

```withRoles("user","moderator","provider") { .. }``` defines a logical **AND**

```WithAnyRole("user","moderator","provider") { .. }``` defines a logical **OR**

```withoutRoles("visitor") { .. }``` defines logical **NOT**
