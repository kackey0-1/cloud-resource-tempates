package com.example.awsapprunner.entity

import org.springframework.beans.support.MutableSortDefinition
import org.springframework.beans.support.PropertyComparator
import java.util.*
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.xml.bind.annotation.XmlElement

@Entity
@Table(name = "vets")
class Vet(
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "vet_specialties",
        joinColumns = [JoinColumn(name = "vet_id")],
        inverseJoinColumns = [JoinColumn(name = "specialty_id")]
    )
    private var specialties: MutableSet<Specialty>? = null
) : Person() {
    protected var specialtiesInternal: MutableSet<Any>?
        protected get() {
            if (specialties == null) {
                specialties = HashSet<Specialty>()
            }
            return specialties
        }
        protected set(specialties) {
            this.specialties = specialties
        }

    @XmlElement
    fun getSpecialties(): List<Specialty> {
        val sortedSpecs: List<Specialty?> = ArrayList(specialtiesInternal)
        PropertyComparator.sort(sortedSpecs, MutableSortDefinition("name", true, true))
        return Collections.unmodifiableList(sortedSpecs)
    }

    val nrOfSpecialties: Int
        get() = specialtiesInternal!!.size

    fun addSpecialty(specialty: Specialty) {
        specialtiesInternal!!.add(specialty)
    }
}
