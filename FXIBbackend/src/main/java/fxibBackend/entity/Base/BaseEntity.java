package fxibBackend.entity.Base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for entities with common fields.

 * This class is marked as MappedSuperclass, indicating that it is not an entity to be
 * persisted on its own, but provides common fields that can be inherited by other
 * entity classes.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * The unique identifier for an entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

