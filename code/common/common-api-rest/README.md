# Example of Common API REST

```java 
package com.example.company.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    // Simple DTO
    public static record CompanyDto(String id, String name, String tenantId) {
    }

    // POST: only users with permission "company.create"
    @PostMapping
    @PreAuthorize("hasAuthority('company.create')")
    public Mono<CompanyDto> createCompany(@RequestBody CompanyDto dto) {
        // Implementation: create and persist company respecting tenant from token or request
        // For demo return same object
        return Mono.just(dto);
    }

    // GET: only allow if tenant matches token (ABAC)
    @GetMapping("/{tenantId}/info")
    @PreAuthorize("@tenantGuard.allowAccess(authentication, #tenantId)")
    public Mono<CompanyDto> getCompanyInfo(@PathVariable String tenantId) {
        // load company info from DB filtered by tenantId...
        // demo:
        return Mono.just(new CompanyDto("company-1", "Acme Corp", tenantId));
    }

    // Hybrid example: permission AND tenant match
    @DeleteMapping("/{tenantId}/{companyId}")
    @PreAuthorize("hasAuthority('company.delete') and @tenantGuard.allowAccess(authentication, #tenantId)")
    public Mono<Void> deleteCompany(@PathVariable String tenantId, @PathVariable String companyId) {
        // perform deletion restricted by tenant
        return Mono.empty();
    }

}
```

## Example of application.yml

```yaml

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:7070/.well-known/jwks.json

```
