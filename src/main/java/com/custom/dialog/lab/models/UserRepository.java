/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.custom.dialog.lab.models;

import java.util.List;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;

/**
 * Demonstrates Spring Data Repository support in Firestore.
 *
 * @author Daniel Zou
 */
public interface UserRepository extends FirestoreReactiveRepository<CustomUser> {
}
