package com.example.courierapp.entities

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "reviews")
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val date: String? = LocalDate.now().toString(),
    var comment: String? = null,
    var rating: Float? = null,
    var reviewerFullName: String? = null,
    // properties...

    @ManyToOne
    @JoinColumn(name = "courier_id")
    var courier: Courier? = null
) {
    // Add other properties...
}
