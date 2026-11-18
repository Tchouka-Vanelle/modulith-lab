# Déploiement Kubernetes du Modulith

Ces manifests suivent les étapes 2.3 à 2.5 du TP. Les commentaires dans les YAML expliquent les
paramètres Kubernetes les plus utiles. Appliquer les manifests dans l'ordre ci-dessous : plusieurs objets
référencent des ConfigMaps, Secrets ou volumes qui doivent déjà exister.

## Préparer l'image

Depuis la racine du projet, après avoir démarré Docker, crée le cluster à trois nœuds :

```bash
KIND_EXPERIMENTAL_DOCKER_NETWORK=formation-kind-net kind create cluster --name formation --config kind-cluster.yaml
./mvnw clean package
./mvnw spring-boot:build-image
kind load docker-image demo-modulith-sb4:0.0.1 --name formation
```

Le manifeste kind épingle `kindest/node:v1.34.0`. Dans l'environnement de réalisation, Docker utilise ZFS
et l'image Kubernetes la plus récente provoque un arrêt du kubelet ; la version épinglée a démarré
correctement. Le réseau `formation-kind-net` contourne un réseau `kind` ancien et vide préexistant sur
cette machine. `KIND_EXPERIMENTAL_DOCKER_NETWORK` est une option kind expérimentale ; sur une machine
où le réseau standard fonctionne, la commande usuelle `kind create cluster --name formation --config
kind-cluster.yaml` suffit.

Le plugin Spring Boot construit une image en couches avec les buildpacks. `BP_JVM_VERSION=21` fixe la
version Java de l'image. Après une modification de code, reconstruire et recharger la même étiquette,
puis lancer `kubectl rollout restart deployment/modulith -n shop` pour remplacer les pods.

## TP 2.3 et 2.4 : H2 en mémoire

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/deployment-h2.yaml
kubectl apply -f k8s/service.yaml
kubectl rollout status deployment/modulith -n shop
```

`deployment-h2.yaml` active les ressources et les sondes, mais ne définit pas le profil `k8s` ni les
variables Postgres : Spring utilise donc H2 en mémoire. Passe le Deployment à trois réplicas avec
`kubectl scale deployment/modulith -n shop --replicas=3`, puis observe les listes incohérentes comme
décrit dans le TP.

Pour appeler l'API depuis le poste :

```bash
kubectl port-forward -n shop svc/modulith 8080:80
curl -s -X POST 'http://localhost:8080/coffees?name=Arabica&price=5'
curl -s http://localhost:8080/coffees
curl -s http://localhost:8080/stocks/1
```

Un port-forward vise un seul pod et ne permet donc pas d'observer l'équilibrage entre plusieurs pods.

## TP 2.5 : bascule vers Postgres persistant

```bash
kubectl apply -f k8s/db-secret.yaml -f k8s/db-config.yaml
kubectl apply -f k8s/postgres-pvc.yaml
kubectl apply -f k8s/postgres.yaml
kubectl rollout status deployment/postgres -n shop
kubectl apply -f k8s/deployment.yaml
kubectl rollout status deployment/modulith -n shop
```

Le PVC réserve 1 Gio ; il peut rester `Pending` jusqu'à ce que le pod Postgres soit planifié. Le
Deployment final active le profil `k8s`, fournit l'URL par ConfigMap et les identifiants par Secret.
`application-k8s.properties` remplace `create-drop` par `update`, car plusieurs instances ne doivent
pas supprimer les données partagées au démarrage.

Les Secrets Kubernetes sont encodés en base64 et ne sont pas chiffrés par défaut. Le mot de passe
`s3cr3t` est une valeur de démonstration du TP : remplace-le pour tout usage réel et ne réutilise pas
ce manifeste de formation avec de vrais identifiants.

## Rôle des fichiers

- `namespace.yaml` crée l'espace isolé `shop`.
- `deployment-h2.yaml` décrit la variante d'apprentissage avec H2 ; `deployment.yaml` décrit l'état final
  avec trois sondes, les ressources du conteneur et Postgres.
- `service.yaml` fournit l'adresse stable `modulith:80` et envoie le trafic vers les pods prêts sur `8080`.
- `db-secret.yaml` contient les identifiants de connexion ; `db-config.yaml` contient le nom de base et
  l'URL JDBC non sensible.
- `postgres-pvc.yaml` demande un stockage persistant ; `postgres.yaml` lance PostgreSQL et son Service.
