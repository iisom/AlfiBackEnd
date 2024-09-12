//package com.galvanize.jwtclient;
//
//import jakarta.persistence.*;
//import java.sql.Timestamp;
//
//@Entity
//@Table(name = "profiles")
//public class Profile {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long profileId;
//
//    private String emailAddress;
//    private String firstName;
//    private String lastName;
//
//    @ManyToOne
//    @JoinColumn(name = "hub_id") // Foreign key column
//    private Hub hub;
//
//    private String phoneNumber;
//    private int sellerRating;
//    private int buyerRating;
//    private Timestamp creationDate;
//
//    // Constructors
//    public Profile() {}
//
//    public Profile(Long profileId, String emailAddress, String firstName, String lastName, Hub hub, String phoneNumber, int sellerRating, int buyerRating, Timestamp creationDate) {
//        this.profileId = profileId;
//        this.emailAddress = emailAddress;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.hub = hub;
//        this.phoneNumber = phoneNumber;
//        this.sellerRating = sellerRating;
//        this.buyerRating = buyerRating;
//        this.creationDate = creationDate;
//    }
//
//    // Getters and Setters
//    public Long getProfileId() {
//        return profileId;
//    }
//
//    public void setProfileId(Long profileId) {
//        this.profileId = profileId;
//    }
//
//    public String getEmailAddress() {
//        return emailAddress;
//    }
//
//    public void setEmailAddress(String emailAddress) {
//        this.emailAddress = emailAddress;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public Hub getHub() {
//        return hub;
//    }
//
//    public void setHub(Hub hub) {
//        this.hub = hub;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public int getSellerRating() {
//        return sellerRating;
//    }
//
//    public void setSellerRating(int sellerRating) {
//        this.sellerRating = sellerRating;
//    }
//
//    public int getBuyerRating() {
//        return buyerRating;
//    }
//
//    public void setBuyerRating(int buyerRating) {
//        this.buyerRating = buyerRating;
//    }
//
//    public Timestamp getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(Timestamp creationDate) {
//        this.creationDate = creationDate;
//    }
//
//    @Override
//    public String toString() {
//        return "Profile{" +
//                "profileId=" + profileId +
//                ", emailAddress='" + emailAddress + '\'' +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", hub=" + hub +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                ", sellerRating=" + sellerRating +
//                ", buyerRating=" + buyerRating +
//                ", creationDate=" + creationDate +
//                '}';
//    }
//}
