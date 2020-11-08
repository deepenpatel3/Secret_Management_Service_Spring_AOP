# Secret Management service using Spring boot and AOP

This project is mainly focused towards building a system, which <b>keeps track of secrets</b> created by and shared with different users. It performs <b>access checks</b> on every 
read operations to make sure the integrity of system is maintained. It also provides <b>statistics such as most_trusted_user, worst_secret_keeper, best_known_secret,</b> etc.

Key points:
- I have used various <b>Data structures</b> such as <b>HashMap, HashSet, LinkedHashMap, ArrayList, etc.</b> and <b>Alogorithms</b> such as <b>Breadth First Search</b> to traverse through the user network of a secret, and find
whether the requesting user has the access or not. 
- I have utilised <b>Aspect Oriented Programming</b> paradigm to increase modularity by allowing the separation of <b>cross-cutting concerns</b>. This concerns include 
  - checking for access 
  - validating user input data
  - taking actions upon exceptions such as IOException, NotAuthorizedException, IllegalArgumentException, etc.
  - updating stats
  - logging
  
## Class diagram of the system:
![image](https://user-images.githubusercontent.com/52833369/98454187-8cca5700-2116-11eb-8105-cff4754d579b.png)
