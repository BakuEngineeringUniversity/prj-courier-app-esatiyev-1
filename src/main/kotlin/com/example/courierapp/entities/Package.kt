package com.example.courierapp.entities

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "packages")
data class Package(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var createdDate: String? = null, // Set the default to the current date
    var packageName: String? = null,
    var weight: Float? = null,
    var price: Float? = null,
    var deliveryMethod: String? = null,
    var deliverAddress: String? = null,
    var pickUpAddress: String? = null,
    var receiverFullName: String? = null,
    var receiverPhone: String? = null,
    var receiverEmail: String? = null,
    var senderFullName: String? = null,
    var senderPhone: String? = null,
    var senderEmail: String? = null,
    var deliveryNote: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    var deliveryStatus: DeliveryStatus = DeliveryStatus.PACKAGE_CREATED,

    //burda
    @OneToMany(mappedBy = "packet", cascade = [CascadeType.ALL])
    val deliveryHistory: MutableList<DeliveryHistory> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null,

    @ManyToOne
    @JoinColumn(name = "courier_id")
    var courier: Courier? = null
) {
    // Add any additional methods or properties specific to the Package entity

    fun updatePackageStatus(newStatus: DeliveryStatus) {
        deliveryStatus = newStatus
        deliveryHistory.add(DeliveryHistory(newStatus, formatDateTime(LocalDateTime.now()), this))
    }

    fun formatDateTime(dateTime: LocalDateTime): String {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateFormatter = DateTimeFormatter.ofPattern("dd.mm.YYYY")

        val timeString = dateTime.format(timeFormatter)
        val dateString = dateTime.format(dateFormatter)

        return "$timeString\n$dateString"
    }
}
