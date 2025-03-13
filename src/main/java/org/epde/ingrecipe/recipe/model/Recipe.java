package org.epde.ingrecipe.recipe.model;

import jakarta.persistence.*;
import lombok.*;
import org.epde.ingrecipe.recipe.enums.RecipeStatus;
import org.epde.ingrecipe.recipe.enums.RecipeStatusConverter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients;

    private String instructions;

    @Builder.Default
    private double ratings = 0.0;

    @Convert(converter = RecipeStatusConverter.class)
    @Column(nullable = false)
    private RecipeStatus status;
}
