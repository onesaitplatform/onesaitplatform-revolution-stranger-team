/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.minsait.onesaitplatform.digitaltwins;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.minsait.onesaitplatform.digitaltwins.comun.helpers.SnackbarHelper;
import com.minsait.onesaitplatform.digitaltwins.comun.helpers.TemperaturaRecyclerViewAdapter;
import com.minsait.onesaitplatform.digitaltwins.vo.Temperatura;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Nodo para el renderizado de la imagen aumentada correspondiente al modelo del aire
 * acondicionado.
 */
@SuppressWarnings({"AndroidApiChecker"})
public class AugmentedImageTermostatoNode2 extends TransformableNode {

    // Tag de este tipo de objetos.
    private static final String TAG = "AugmentedImageTermostatoNode2";

    // La imagen aumentada representada por este nodo.
    private AugmentedImage image;
    // Nodo del objeto que se renderizará.
    private AnchorNode nodo;
    private Scene sceneParent;
    // Contiene el modelo a renderizar.
    private CompletableFuture<ModelRenderable> modeloRenderizable;

    public AugmentedImageTermostatoNode2(Context context, TransformationSystem transformationSystem, Scene parent) {
        super(transformationSystem);
        sceneParent = parent;
        // Cargamos el modelo renderizable a partir del fichero sfb.
        modeloRenderizable =
                ModelRenderable.builder()
                        .setSource(context, Uri.parse("termostato.sfb"))
                        .build();
    }

    /**
     * Se llama a este método cuando se detecta una imagen aumentada y debemos renderizarla. En ese
     * momento se crea un nodo para añadirlo al árbol de Sceneform a partir de la imagen detectada.
     */
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    public void setImage(AugmentedImage image) {
        this.image = image;

        // Initialize mazeNode and set its parents and the Renderable.
        // If any of the models are not loaded, process this function
        // until they all are loaded.
        if (!modeloRenderizable.isDone()) {
            CompletableFuture.allOf(modeloRenderizable)
                    .thenAccept((Void aVoid) -> setImage(image))
                    .exceptionally(
                            throwable -> {
                                Log.e(TAG, "Exception loading", throwable);
                                return null;
                            });
            return;
        }

        // Creamos finalmente el nodo y añadimos el modelo a renderizar.
        nodo = new AnchorNode(image.createAnchor(image.getCenterPose()));
        nodo.setParent(sceneParent);

        this.setParent(nodo);
        this.setRenderable(modeloRenderizable.getNow(null));
        this.select();

        // Cambiamos la dirección a la que mira el modelo para que salga correctamente según el marcador.
        setLookDirection(new Vector3(90.00f, 0.00f, 45.00f));

        ViewRenderable.builder()
                .setView(sceneParent.getView().getContext(), R.layout.btn_termostato_mostrar_tabla)
                .build()
                .thenAccept(renderable -> {

                    ImageView ib = (ImageView) renderable.getView();

                    AnchorNode nodoBoton = new AnchorNode();
                    nodoBoton.setParent(AugmentedImageTermostatoNode2.this);
                    Vector3 posicion = new Vector3(getLocalPosition());
                    posicion.z -= 0.055f;
                    posicion.y += 0.055f;
                    nodoBoton.setLocalPosition(posicion);
                    nodoBoton.setLookDirection(new Vector3(-90.00f, 0.00f, 180.00f));
                    nodoBoton.setRenderable(renderable);

                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Si tenemos visible la tabla, la eliminamos.
                            if (getChildren().size() > 2) {
                                for (int i = 2; getChildren().size() > 2; i++) {
                                    Node nodo = getChildren().get(i);
                                    nodo.setParent(null);
                                    sceneParent.removeChild(nodo);
                                    i--;
                                }
                            } else {
                                ViewRenderable.builder()
                                        .setView(sceneParent.getView().getContext(), R.layout.fragment_temperatura_list)
                                        .build()
                                        .thenAccept(renderable -> {
                                            List<Temperatura> lista = new ArrayList<Temperatura>();
                                            lista.add(new Temperatura("28/09/2019", 26.40f));
                                            lista.add(new Temperatura("29/09/2019", 25.40f));
                                            lista.add(new Temperatura("30/09/2019", 25.30f));
                                            lista.add(new Temperatura("01/10/2019", 24.40f));
                                            lista.add(new Temperatura("02/10/2019", 25.40f));

                                            RecyclerView rv = (RecyclerView) renderable.getView();
                                            rv.setAdapter(new TemperaturaRecyclerViewAdapter(sceneParent.getView().getContext(), lista));

                                            AnchorNode nodoRV = new AnchorNode();
                                            Vector3 posicion = new Vector3(getLocalPosition());
                                            posicion.y -= 0.136f * lista.size();
                                            nodoRV.setLocalPosition(posicion);
                                            nodoRV.setLookDirection(new Vector3(45.00f, 0.00f, 0.00f));

                                            nodoRV.setParent(AugmentedImageTermostatoNode2.this);
                                            nodoRV.setRenderable(renderable);
                                        });
                            }
                        }
                    });
                });
    }

    public AugmentedImage getImage() {
        return image;
    }
}