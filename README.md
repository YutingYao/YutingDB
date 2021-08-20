# TensorflowKeras 常用命令
Study Notes of Yuting
最近因敲代码，视力显著下降，故断更。。有空再继续。。

# Section 1 - Packages

## tf.keras.applications
### tf.keras.applications.resnet
### tf.keras.applications.ResNet50
### tf.keras.applications.ResNet50V2
### tf.keras.applications.ResNet101
### tf.keras.applications.ResNet101V2
### tf.keras.applications.ResNet152
### tf.keras.applications.ResNet152V2
### tf.keras.applications.MobileNet
### tf.keras.applications.MobileNetV2
### tf.keras.applications.imagenet_utils
### tf.keras.applications.inceptionV3
### tf.keras.applications.inceptionResNetV2
### tf.keras.applications.vgg19
### tf.keras.applications.vgg16
### tf.keras.applications.efficientNet
### tf.keras.applications.EfficientNetB0
### tf.keras.applications.EfficientNetB1
### tf.keras.applications.EfficientNetB2
### tf.keras.applications.EfficientNetB3
### tf.keras.applications.EfficientNetB4
### tf.keras.applications.EfficientNetB5
### tf.keras.applications.EfficientNetB6
### tf.keras.applications.EfficientNetB7
### tf.keras.applications.Xception
### tf.keras.applications.DenseNet201
### tf.keras.applications.DenseNet169
### tf.keras.applications.DenseNet121

## tf.keras.activations
### tf.keras.activations.relu
### tf.keras.activations.sigmoid
### tf.keras.activations.softmax
### tf.keras.activations.softplus
### tf.keras.activations.softsign
### tf.keras.activations.tanh
### tf.keras.activations.selu
### tf.keras.activations.elu
### tf.keras.activations.exponential
### tf.keras.activations.serialize
### tf.keras.activations.deserialize
### tf.keras.activations.get


## tf.keras.backend
### tf.keras.backend.int_shape
### tf.keras.backend.clear_session
### tf.keras.backend.random_normal
### tf.keras.backend.epsilon
### tf.keras.backend.get_value
### tf.keras.backend.set_value
### tf.keras.backend.clip
### tf.keras.backend.batch_get_value
### tf.keras.backend.image_data_format
### tf.keras.backend.learning_phase
### tf.keras.backend.depthwise_conv2d
### tf.keras.backend.bias_add

## tf.keras.callbacks
### tf.keras.callbacks.TensorBoard
### tf.keras.callbacks.ModelCheckpoint
### tf.keras.callbacks.EarlyStopping
### tf.keras.callbacks.LearningRateScheduler
### tf.keras.callbacks.History
### tf.keras.callbacks.ReduceLROnPlateau

## tf.keras.datasets
### tf.keras.datasets.boston_housing
### tf.keras.datasets.mnist
### tf.keras.datasets.cifar10
### tf.keras.datasets.cifar100
### tf.keras.datasets.fashion_mnist


## tf.keras.experimental
### tf.keras.experimental.CosineDecay
### tf.keras.experimental.cluster
### tf.keras.experimental.PeepholeLSTMCell
### tf.keras.experimental.export_saved_model
### tf.keras.experimental.load_from_saved_model

## tf.keras.initializers
### tf.keras.initializers.RandomNormal
### tf.keras.initializers.RandomUniform
### tf.keras.initializers.TruncatedNormal
### tf.keras.initializers.Zeros
### tf.keras.initializers.Ones
### tf.keras.initializers.GlorotNormal
### tf.keras.initializers.GlorotUniform
### tf.keras.initializers.VarianceScaling
### tf.keras.initializers.Orthogonal
### tf.keras.initializers.Identity
### tf.keras.initializers.HeUniform
### tf.keras.initializers.HeNormal
### tf.keras.initializers.Constant

## tf.keras.Input


## tf.keras.layers
### tf.keras.layers.Conv2D
### tf.keras.layers.DepthwiseConv2D
### tf.keras.layers.Conv2DTranspose
### tf.keras.layers.BatchNormalization
### tf.keras.layers.ReLU
### tf.keras.layers.AveragePooling2D
### tf.keras.layers.GlobalAveragePooling2D
### tf.keras.layers.GlobalAveragePooling1D
### tf.keras.layers.GlobalMaxPooling1D
### tf.keras.layers.UpSamling2D
### tf.keras.layers.Concatenate
### tf.keras.layers.Input
### tf.keras.layers.InputSpec
### tf.keras.layers.Lambda
### tf.keras.layers.Activation
### tf.keras.layers.Reshape
### tf.keras.layers.ZeroPadding2D
### tf.keras.layers.Add
### tf.keras.layers.Dropout
### tf.keras.layers.BatchNormalization
### tf.keras.layers.Embedding
### tf.keras.layers.Masking
### tf.keras.layers.Conv1D
### tf.keras.layers.Dense
### tf.keras.layers.Bidirectional
### tf.keras.layers.LSTM
### tf.keras.layers.serialize
### tf.keras.layers.Flatten
### tf.keras.layers.Layer.get_weights
### tf.keras.layers.Layer.set_weights
### tf.keras.layers.Layer.weights
### tf.keras.layers.Layer.trainable_weights
### tf.keras.layers.Layer.non_trainable_weights
### tf.keras.layers.Layer.trainable
### tf.keras.layers.Layer.add_loss
### tf.keras.layers.Layer.add_metric
### tf.keras.layers.Layer.losses
### tf.keras.layers.Layer.metrics
### tf.keras.layers.Layer.dynamic
### tf.keras.layers.experimental.preprocessing.Resizing
### tf.keras.layers.experimental.preprocessing.RandomFourierFeatures
### tf.keras.layers.experimental.preprocessing.StringLookup
### tf.keras.layers.experimental.preprocessing.TextVectorization
### tf.keras.layers.experimental.preprocessing.InterLookup
### tf.keras.layers.experimental.preprocessing.Normalization
### tf.keras.layers.experimental.preprocessing.CenterCrop
### tf.keras.layers.experimental.preprocessing.Rescaling



## tf.keras.losses
### tf.keras.losses.categorical_crossentropy
### tf.keras.losses.SparseCategoticalCrossentropy
### tf.keras.losses.MeanSquareError
### tf.keras.losses.kl_divergence

## tf.keras.models
### tf.keras.models.load_model
### tf.keras.models.save_model
### tf.keras.models.Sequential
### tf.keras.models.clone_model
### tf.keras.models.model_from_json
### tf.keras.models.model_from_config
### tf.keras.models.model_to_json

## tf.keras.metrics
### tf.keras.metrics.AUC
### tf.keras.metrics.SparseCategoticalAccuracy
### tf.keras.metrics.Mean
### tf.keras.metrics.BinaryAccuracy
### tf.keras.metrics.Pricision
### tf.keras.metrics.Recall
### tf.keras.metrics.PricisionAtRecall
### tf.keras.metrics.CategoticalAccuracy

## tf.keras.optimizers
### tf.keras.optimizers.Adam
### tf.keras.optimizers.schedules.ExponentialDecay
### tf.keras.optimizers.RMSprop
### tf.keras.optimizers.SGD
### tf.keras.optimizers.Adadelta

## tf.keras.preprocessing
### tf.keras.preprocessing.image.array_to_img
### tf.keras.preprocessing.image.img_to array
### tf.keras.preprocessing.image.load_img
### tf.keras.preprocessing.text_dataset_from_dictionary
### tf.keras.preprocessing.image_dataset_from_dictionary
### tf.keras.preprocessing.sequence.pad_sequences

## tf.keras.regularizers
### tf.keras.regularizers.L1
### tf.keras.regularizers.L2

## tf.keras.Sequential

## tf.keras.utils
### tf.keras.utils.layer_utils.get_source_inputs
### tf.keras.utils.data_utils.get_file
### tf.keras.utils.to_categorical
### tf.keras.utils.Sequence
### tf.keras.utils.get_file
### tf.keras.utils.CustomObjectScope
### tf.keras.utils.deserialize_keras_object
### tf.keras.utils.serialize_keras_object

```python

class AdaMatch(keras.Model):
    def __init__(self, model, total_steps, tau=0.9):
        super(AdaMatch, self).__init__()
        self.model = model
        self.tau = tau  # Denotes the confidence threshold
        self.loss_tracker = tf.keras.metrics.Mean(name="loss")
        self.total_steps = total_steps
        self.current_step = tf.Variable(0, dtype="int64")

    @property
    def metrics(self):
        return [self.loss_tracker]

    # This is a warmup schedule to update the weight of the
    # loss contributed by the target unlabeled samples. More
    # on this in the text.
    def compute_mu(self):
        pi = tf.constant(np.pi, dtype="float32")
        step = tf.cast(self.current_step, dtype="float32")
        return 0.5 - tf.cos(tf.math.minimum(pi, (2 * pi * step) / self.total_steps)) / 2

    def train_step(self, data):
        ## Unpack and organize the data ##
        source_ds, target_ds = data
        (source_w, source_labels), (source_s, _) = source_ds
        (
            (target_w, _),
            (target_s, _),
        ) = target_ds  # Notice that we are NOT using any labels here.

        combined_images = tf.concat([source_w, source_s, target_w, target_s], 0)
        combined_source = tf.concat([source_w, source_s], 0)

        total_source = tf.shape(combined_source)[0]
        total_target = tf.shape(tf.concat([target_w, target_s], 0))[0]

        with tf.GradientTape() as tape:
            ## Forward passes ##
            combined_logits = self.model(combined_images, training=True)
            z_d_prime_source = self.model(
                combined_source, training=False
            )  # No BatchNorm update.
            z_prime_source = combined_logits[:total_source]

            ## 1. Random logit interpolation for the source images ##
            lambd = tf.random.uniform((total_source, 10), 0, 1)
            final_source_logits = (lambd * z_prime_source) + (
                (1 - lambd) * z_d_prime_source
            )

            ## 2. Distribution alignment (only consider weakly augmented images) ##
            # Compute softmax for logits of the WEAKLY augmented SOURCE images.
            y_hat_source_w = tf.nn.softmax(final_source_logits[: tf.shape(source_w)[0]])

            # Extract logits for the WEAKLY augmented TARGET images and compute softmax.
            logits_target = combined_logits[total_source:]
            logits_target_w = logits_target[: tf.shape(target_w)[0]]
            y_hat_target_w = tf.nn.softmax(logits_target_w)

            # Align the target label distribution to that of the source.
            expectation_ratio = tf.reduce_mean(y_hat_source_w) / tf.reduce_mean(
                y_hat_target_w
            )
            y_tilde_target_w = tf.math.l2_normalize(
                y_hat_target_w * expectation_ratio, 1
            )

            ## 3. Relative confidence thresholding ##
            row_wise_max = tf.reduce_max(y_hat_source_w, axis=-1)
            final_sum = tf.reduce_mean(row_wise_max, 0)
            c_tau = self.tau * final_sum
            mask = tf.reduce_max(y_tilde_target_w, axis=-1) >= c_tau

            ## Compute losses (pay attention to the indexing) ##
            source_loss = compute_loss_source(
                source_labels,
                final_source_logits[: tf.shape(source_w)[0]],
                final_source_logits[tf.shape(source_w)[0] :],
            )
            target_loss = compute_loss_target(
                y_tilde_target_w, logits_target[tf.shape(target_w)[0] :], mask
            )

            t = self.compute_mu()  # Compute weight for the target loss
            total_loss = source_loss + (t * target_loss)
            self.current_step.assign_add(
                1
            )  # Update current training step for the scheduler

        gradients = tape.gradient(total_loss, self.model.trainable_variables)
        self.optimizer.apply_gradients(zip(gradients, self.model.trainable_variables))

        self.loss_tracker.update_state(total_loss)
        return {"loss": self.loss_tracker.result()}
```
```python
def wide_basic(x, n_input_plane, n_output_plane, stride):
    conv_params = [[3, 3, stride, "same"], [3, 3, (1, 1), "same"]]

    n_bottleneck_plane = n_output_plane

    # Residual block
    for i, v in enumerate(conv_params):
        if i == 0:
            if n_input_plane != n_output_plane:
                x = layers.BatchNormalization()(x)
                x = layers.Activation("relu")(x)
                convs = x
            else:
                convs = layers.BatchNormalization()(x)
                convs = layers.Activation("relu")(convs)
            convs = layers.Conv2D(
                n_bottleneck_plane,
                (v[0], v[1]),
                strides=v[2],
                padding=v[3],
                kernel_initializer=INIT,
                kernel_regularizer=regularizers.l2(WEIGHT_DECAY),
                use_bias=False,
            )(convs)
        else:
            convs = layers.BatchNormalization()(convs)
            convs = layers.Activation("relu")(convs)
            convs = layers.Conv2D(
                n_bottleneck_plane,
                (v[0], v[1]),
                strides=v[2],
                padding=v[3],
                kernel_initializer=INIT,
                kernel_regularizer=regularizers.l2(WEIGHT_DECAY),
                use_bias=False,
            )(convs)

    # Shortcut connection: identity function or 1x1
    # convolutional
    #  (depends on difference between input & output shape - this
    #   corresponds to whether we are using the first block in
    #   each
    #   group; see `block_series()`).
    if n_input_plane != n_output_plane:
        shortcut = layers.Conv2D(
            n_output_plane,
            (1, 1),
            strides=stride,
            padding="same",
            kernel_initializer=INIT,
            kernel_regularizer=regularizers.l2(WEIGHT_DECAY),
            use_bias=False,
        )(x)
    else:
        shortcut = x

    return layers.Add()([convs, shortcut])


# Stacking residual units on the same stage
def block_series(x, n_input_plane, n_output_plane, count, stride):
    x = wide_basic(x, n_input_plane, n_output_plane, stride)
    for i in range(2, int(count + 1)):
        x = wide_basic(x, n_output_plane, n_output_plane, stride=1)
    return x


def get_network(image_size=32, num_classes=10):
    n = (DEPTH - 4) / 6
    n_stages = [16, 16 * WIDTH_MULT, 32 * WIDTH_MULT, 64 * WIDTH_MULT]

    inputs = keras.Input(shape=(image_size, image_size, 3))
    x = layers.experimental.preprocessing.Rescaling(scale=1.0 / 255)(inputs)

    conv1 = layers.Conv2D(
        n_stages[0],
        (3, 3),
        strides=1,
        padding="same",
        kernel_initializer=INIT,
        kernel_regularizer=regularizers.l2(WEIGHT_DECAY),
        use_bias=False,
    )(x)

    ## Add wide residual blocks ##

    conv2 = block_series(
        conv1,
        n_input_plane=n_stages[0],
        n_output_plane=n_stages[1],
        count=n,
        stride=(1, 1),
    )  # Stage 1

    conv3 = block_series(
        conv2,
        n_input_plane=n_stages[1],
        n_output_plane=n_stages[2],
        count=n,
        stride=(2, 2),
    )  # Stage 2

    conv4 = block_series(
        conv3,
        n_input_plane=n_stages[2],
        n_output_plane=n_stages[3],
        count=n,
        stride=(2, 2),
    )  # Stage 3

    batch_norm = layers.BatchNormalization()(conv4)
    relu = layers.Activation("relu")(batch_norm)

    # Classifier
    trunk_outputs = layers.GlobalAveragePooling2D()(relu)
    outputs = layers.Dense(
        num_classes, kernel_regularizer=regularizers.l2(WEIGHT_DECAY)
    )(trunk_outputs)

    return keras.Model(inputs, outputs)
```
```python
reduce_lr = keras.experimental.CosineDecay(LEARNING_RATE, TOTAL_STEPS, 0.25)
optimizer = keras.optimizers.Adam(reduce_lr)

adamatch_trainer = AdaMatch(model=wrn_model, total_steps=TOTAL_STEPS)
adamatch_trainer.compile(optimizer=optimizer)

total_ds = tf.data.Dataset.zip((final_source_ds, final_target_ds))
adamatch_trainer.fit(total_ds, epochs=EPOCHS)
```

#### tensorflow.keras.backend
#### tensorflow.keras.regularizers

#### tensorflow.keras.activations
relu

#### tensorflow.keras.applications
imagenet_utils.preprocess_input
resnet
inception_v3
vgg19

## resnet50
ResNet50

## resnet_cifar10_v2
#### tensorflow.keras.metrics
#### tensorflow.keras.

#### tensorflow.keras.models
Model
load_model
Sequential
```python
input = layers.Input(shape=(28, 28, 1))

# Encoder
x = layers.Conv2D(32, (3, 3), activation="relu", padding="same")(input)
x = layers.MaxPooling2D((2, 2), padding="same")(x)
x = layers.Conv2D(32, (3, 3), activation="relu", padding="same")(x)
x = layers.MaxPooling2D((2, 2), padding="same")(x)

# Decoder
x = layers.Conv2DTranspose(32, (3, 3), strides=2, activation="relu", padding="same")(x)
x = layers.Conv2DTranspose(32, (3, 3), strides=2, activation="relu", padding="same")(x)
x = layers.Conv2D(1, (3, 3), activation="sigmoid", padding="same")(x)

# Autoencoder
autoencoder = Model(input, x)
autoencoder.compile(optimizer="adam", loss="binary_crossentropy")
autoencoder.summary()
```

```python
trained_model = tf.keras.models.load_model(SAVED_MODEL_DIR)
trained_model.summary()
```

```python
# docs_infra: no_execute
model = keras.models.load_model(save_path)
```

#### tensorflow.keras.callbacks
Callback
TensorBoard
ModelCheckpoint
EarlyStopping
LearningRateScheduler

```python
# Define callbacks.
checkpoint_cb = keras.callbacks.ModelCheckpoint(
    "3d_image_classification.h5", save_best_only=True
)
early_stopping_cb = keras.callbacks.EarlyStopping(monitor="val_acc", patience=15)
```

```python
model.fit(x=x_train[:100], y=y_train[:100], validation_split=0.2, epochs=1)
```

```python
callbacks = [
      tf.keras.callbacks.TensorBoard(log_dir=TENSORBOARD_LOGS_DIR),
      tf.keras.callbacks.ModelCheckpoint(MODEL_CHECKPOINT_DIR, save_best_only=True),
      tf.keras.callbacks.EarlyStopping(monitor="loss", min_delta=0.001, patience=3),
]

model.fit(
      x=x_train, y=y_train, epochs=100, validation_split=0.2, callbacks=callbacks,
)

model.save(SAVED_MODEL_DIR)
```

```python
callbacks = [
    # TensorBoard will store logs for each epoch and graph performance for us.
    keras.callbacks.TensorBoard(log_dir=tensorboard_path, histogram_freq=1),
    # ModelCheckpoint will save models after each epoch for retrieval later.
    keras.callbacks.ModelCheckpoint(checkpoint_path),
    # EarlyStopping will terminate training when val_loss ceases to improve.
    keras.callbacks.EarlyStopping(monitor="val_loss", patience=3),
]

```

```python
%load_ext tensorboard
%tensorboard --logdir $TENSORBOARD_LOGS_DIR
```

```python
!#docs_infra: no_execute
!tensorboard dev upload --logdir "gs://keras-examples-jonah/logs/fit" --name "Guide MNIST"
```

#### tensorflow.keras.datasets

mnist

```python

(x_train, y_train), (_, _) = tf.keras.datasets.mnist.load_data()
x_train = x_train.reshape((60000, 28 * 28))
x_train = x_train.astype("float32") / 255

```



#### tensorflow.keras.utils
multi_gpu_model
Sequence
to_categorical
conv_utils
```python
    if input_tensor is not None:
        inputs = get_source_inputs(input_tensor)
    else:
        inputs = img_input
```
```python
# Download url of normal CT scans.
url = "https://github.com/hasibzunair/3D-image-classification-tutorial/releases/download/v0.2/CT-0.zip"
filename = os.path.join(os.getcwd(), "CT-0.zip")
keras.utils.get_file(filename, url)

# Download url of abnormal CT scans.
url = "https://github.com/hasibzunair/3D-image-classification-tutorial/releases/download/v0.2/CT-23.zip"
filename = os.path.join(os.getcwd(), "CT-23.zip")
keras.utils.get_file(filename, url)
```


#### tensorflow.keras.losses
sparse_categorical_crossentropy
categorical_crossentropy

```python
def compute_loss_source(source_labels, logits_source_w, logits_source_s):
    loss_func = keras.losses.CategoricalCrossentropy(from_logits=True)
    # First compute the losses between original source labels and
    # predictions made on the weakly and strongly augmented versions
    # of the same images.
    w_loss = loss_func(source_labels, logits_source_w)
    s_loss = loss_func(source_labels, logits_source_s)
    return w_loss + s_loss


def compute_loss_target(target_pseudo_labels_w, logits_target_s, mask):
    loss_func = keras.losses.CategoricalCrossentropy(from_logits=True, reduction="none")
    target_pseudo_labels_w = tf.stop_gradient(target_pseudo_labels_w)
    # For calculating loss for the target samples, we treat the pseudo labels
    # as the ground-truth. These are not considered during backpropagation
    # which is a standard SSL practice.
    target_loss = loss_func(target_pseudo_labels_w, logits_target_s)

    # More on `mask` later.
    mask = tf.cast(mask, target_loss.dtype)
    target_loss *= mask
    return tf.reduce_mean(target_loss, 0)
```

#### tensorflow.keras.layers 
Input
Lambda
Activation
Concatenate
Add
Dropout
BatchNormalization
Conv2D
DepthwiseConv2D
ZeroPadding2D
GlobalAveragePooling2D
Reshape
Embedding
```python
def get_model(width=128, height=128, depth=64):
    """Build a 3D convolutional neural network model."""

    inputs = keras.Input((width, height, depth, 1))

    x = layers.Conv3D(filters=64, kernel_size=3, activation="relu")(inputs)
    x = layers.MaxPool3D(pool_size=2)(x)
    x = layers.BatchNormalization()(x)

    x = layers.Conv3D(filters=64, kernel_size=3, activation="relu")(x)
    x = layers.MaxPool3D(pool_size=2)(x)
    x = layers.BatchNormalization()(x)

    x = layers.Conv3D(filters=128, kernel_size=3, activation="relu")(x)
    x = layers.MaxPool3D(pool_size=2)(x)
    x = layers.BatchNormalization()(x)

    x = layers.Conv3D(filters=256, kernel_size=3, activation="relu")(x)
    x = layers.MaxPool3D(pool_size=2)(x)
    x = layers.BatchNormalization()(x)

    x = layers.GlobalAveragePooling3D()(x)
    x = layers.Dense(units=512, activation="relu")(x)
    x = layers.Dropout(0.3)(x)

    outputs = layers.Dense(units=1, activation="sigmoid")(x)

    # Define the model.
    model = keras.Model(inputs, outputs, name="3dcnn")
    return model
model = get_model(width=128, height=128, depth=64)
model.summary()

# Compile model.
initial_learning_rate = 0.0001
lr_schedule = keras.optimizers.schedules.ExponentialDecay(
    initial_learning_rate, decay_steps=100000, decay_rate=0.96, staircase=True
)
model.compile(
    loss="binary_crossentropy",
    optimizer=keras.optimizers.Adam(learning_rate=lr_schedule),
    metrics=["acc"],
)

# Define callbacks.
checkpoint_cb = keras.callbacks.ModelCheckpoint(
    "3d_image_classification.h5", save_best_only=True
)
early_stopping_cb = keras.callbacks.EarlyStopping(monitor="val_acc", patience=15)

# Train the model, doing validation at the end of each epoch
epochs = 100
model.fit(
    train_dataset,
    validation_data=validation_dataset,
    epochs=epochs,
    shuffle=True,
    verbose=2,
    callbacks=[checkpoint_cb, early_stopping_cb],
)

# Load best weights.
model.load_weights("3d_image_classification.h5")
prediction = model.predict(np.expand_dims(x_val[0], axis=0))[0]

```

```python

model = tf.keras.Sequential(
    [
        tf.keras.layers.Dense(512, activation="relu", input_shape=(28 * 28,)),
        tf.keras.layers.Dropout(0.2),
        tf.keras.layers.Dense(10, activation="softmax"),
    ]
)

model.compile(
    loss="sparse_categorical_crossentropy",
    optimizer=tf.keras.optimizers.Adam(),
    metrics=["accuracy"],
)
```

```python
def create_model():
    model = keras.Sequential(
        [
            keras.Input(shape=(28, 28)),
            layers.experimental.preprocessing.Rescaling(1.0 / 255),
            layers.Reshape(target_shape=(28, 28, 1)),
            layers.Conv2D(32, 3, activation="relu"),
            layers.MaxPooling2D(2),
            layers.Conv2D(32, 3, activation="relu"),
            layers.MaxPooling2D(2),
            layers.Conv2D(32, 3, activation="relu"),
            layers.Flatten(),
            layers.Dense(128, activation="relu"),
            layers.Dense(10),
        ]
    )

    model.compile(
        optimizer=keras.optimizers.Adam(),
        loss=keras.losses.SparseCategoricalCrossentropy(from_logits=True),
        metrics=keras.metrics.SparseCategoricalAccuracy(),
    )
    return model

model = create_model()
 
model.fit(x_train, y_train, epochs=20, batch_size=128, validation_split=0.1)
```

#### tensorflow.keras.utils
layer_utils.get_source_inputs
data_utils.get_file

## os
### os.path.sep
```python
# Path to the data directory
data_dir = Path("./captcha_images_v2/")

# Get list of all the images
images = sorted(list(map(str, list(data_dir.glob("*.png")))))
labels = [img.split(os.path.sep)[-1].split(".png")[0] for img in images]
characters = set(char for label in labels for char in label)

print("Number of images found: ", len(images))
print("Number of labels found: ", len(labels))
print("Number of unique characters: ", len(characters))
print("Characters present: ", characters)
```
### os.path.getsize
```python
def get_gzipped_model_size(model):
  # It returns the size of the gzipped model in bytes.
  import os
  import zipfile

  keras_file = save_model_file(model)

  _, zipped_file = tempfile.mkstemp('.zip')
  with zipfile.ZipFile(zipped_file, 'w', compression=zipfile.ZIP_DEFLATED) as f:
    f.write(keras_file)
  return os.path.getsize(zipped_file)
```

### os.path.getctime
```python
# Prepare a directory to store all the checkpoints.
checkpoint_dir = "./ckpt"
if not os.path.exists(checkpoint_dir):
    os.makedirs(checkpoint_dir)


def make_or_restore_model():
    # Either restore the latest model, or create a fresh one
    # if there is no checkpoint available.
    checkpoints = [checkpoint_dir + "/" + name for name in os.listdir(checkpoint_dir)]
    if checkpoints:
        latest_checkpoint = max(checkpoints, key=os.path.getctime)
        print("Restoring from", latest_checkpoint)
        return keras.models.load_model(latest_checkpoint)
    print("Creating a new model")
    return get_compiled_model()
```

### os.path.abspath
```python
url = "https://github.com/soon-yau/stylegan_keras/releases/download/keras_example_v1.0/stylegan_128x128.ckpt.zip"

weights_path = keras.utils.get_file(
    "stylegan_128x128.ckpt.zip",
    url,
    extract=True,
    cache_dir=os.path.abspath("."),
    cache_subdir="pretrained",
)
```

### os.path.join
```python
train_size = 30000
valid_size = 5000
captions_per_image = 2
images_per_file = 2000

train_image_paths = image_paths[:train_size]
num_train_files = int(np.ceil(train_size / images_per_file))
train_files_prefix = os.path.join(tfrecords_dir, "train")

valid_image_paths = image_paths[-valid_size:]
num_valid_files = int(np.ceil(valid_size / images_per_file))
valid_files_prefix = os.path.join(tfrecords_dir, "valid")
```

```python
train_dataset = get_dataset(os.path.join(tfrecords_dir, "train-*.tfrecord"), batch_size)
valid_dataset = get_dataset(os.path.join(tfrecords_dir, "valid-*.tfrecord"), batch_size)
```Z


```python
zip_file = keras.utils.get_file(
    fname="cora.tgz",
    origin="https://linqs-data.soe.ucsc.edu/public/lbc/cora.tgz",
    extract=True,
)
data_dir = os.path.join(os.path.dirname(zip_file), "cora")
```

```python
TENSORBOARD_LOGS_DIR = os.path.join(GCS_BASE_PATH, "logs")
MODEL_CHECKPOINT_DIR = os.path.join(GCS_BASE_PATH, "checkpoints")
SAVED_MODEL_DIR = os.path.join(GCS_BASE_PATH, "saved_model")
```

```python
# Download url of normal CT scans.
url = "https://github.com/hasibzunair/3D-image-classification-tutorial/releases/download/v0.2/CT-0.zip"
filename = os.path.join(os.getcwd(), "CT-0.zip")
keras.utils.get_file(filename, url)

# Download url of abnormal CT scans.
url = "https://github.com/hasibzunair/3D-image-classification-tutorial/releases/download/v0.2/CT-23.zip"
filename = os.path.join(os.getcwd(), "CT-23.zip")
keras.utils.get_file(filename, url)

# Make a directory to store the data.
os.makedirs("MosMedData")

```

```python
# Folder "CT-0" consist of CT scans having normal lung tissue,
# no CT-signs of viral pneumonia.
normal_scan_paths = [
    os.path.join(os.getcwd(), "MosMedData/CT-0", x)
    for x in os.listdir("MosMedData/CT-0")
]
# Folder "CT-23" consist of CT scans having several ground-glass opacifications,
# involvement of lung parenchyma.
abnormal_scan_paths = [
    os.path.join(os.getcwd(), "MosMedData/CT-23", x)
    for x in os.listdir("MosMedData/CT-23")
]
```

### os.path.expanduser
```python
path_to_glove_file = os.path.join(
    os.path.expanduser("~"), ".keras/datasets/glove.6B.100d.txt"
)

embeddings_index = {}
with open(path_to_glove_file) as f:
    for line in f:
        word, coefs = line.split(maxsplit=1)
        coefs = np.fromstring(coefs, "f", sep=" ")
        embeddings_index[word] = coefs

print("Found %s word vectors." % len(embeddings_index))
```

```python
# Get the data from https://www.kaggle.com/kongaevans/speaker-recognition-dataset/download
# and save it to the 'Downloads' folder in your HOME directory
DATASET_ROOT = os.path.join(os.path.expanduser("~"), "Downloads/16000_pcm_speeches")
```


### os.path.exists
```python
if not os.path.exists(images_dir):
    image_zip = tf.keras.utils.get_file(
        "train2014.zip",
        cache_dir=os.path.abspath("."),
        origin="http://images.cocodataset.org/zips/train2014.zip",
        extract=True,
    )
    os.remove(image_zip)
```

```python
# Download image files
if not os.path.exists(images_dir):
    image_zip = tf.keras.utils.get_file(
        "images.zip", cache_dir=os.path.abspath("."), origin=images_url, extract=True,
    )
    os.remove(image_zip)

# Download caption annotation files
if not os.path.exists(annotations_dir):
    annotation_zip = tf.keras.utils.get_file(
        "captions.zip",
        cache_dir=os.path.abspath("."),
        origin=annotations_url,
        extract=True,
    )
    os.remove(annotation_zip)
```
### os.path.isdir
```python
# Get the list of all noise files
noise_paths = []
for subdir in os.listdir(DATASET_NOISE_PATH):
    subdir_path = Path(DATASET_NOISE_PATH) / subdir
    if os.path.isdir(subdir_path):
        noise_paths += [
            os.path.join(subdir_path, filepath)
            for filepath in os.listdir(subdir_path)
            if filepath.endswith(".wav")
        ]

print(
    "Found {} files belonging to {} directories".format(
        len(noise_paths), len(os.listdir(DATASET_NOISE_PATH))
    )
)
```

### os.system
```python
command = (
    "for dir in `ls -1 " + DATASET_NOISE_PATH + "`; do "
    "for file in `ls -1 " + DATASET_NOISE_PATH + "/$dir/*.wav`; do "
    "sample_rate=`ffprobe -hide_banner -loglevel panic -show_streams "
    "$file | grep sample_rate | cut -f2 -d=`; "
    "if [ $sample_rate -ne 16000 ]; then "
    "ffmpeg -hide_banner -loglevel panic -y "
    "-i $file -ar 16000 temp.wav; "
    "mv temp.wav $file; "
    "fi; done; done"
)
os.system(command)
```

### os.makedirs
```python
os.makedirs("celeba_gan")

url = "https://drive.google.com/uc?id=1O7m1010EJjLE5QxLZiM9Fpjs7Oj6e684"
output = "celeba_gan/data.zip"
gdown.download(url, output, quiet=True)

with ZipFile("celeba_gan/data.zip", "r") as zipobj:
    zipobj.extractall("celeba_gan")
```

```python
# Prepare a directory to store all the checkpoints.
checkpoint_dir = "./ckpt"
if not os.path.exists(checkpoint_dir):
    os.makedirs(checkpoint_dir)


def make_or_restore_model():
    # Either restore the latest model, or create a fresh one
    # if there is no checkpoint available.
    checkpoints = [checkpoint_dir + "/" + name for name in os.listdir(checkpoint_dir)]
    if checkpoints:
        latest_checkpoint = max(checkpoints, key=os.path.getctime)
        print("Restoring from", latest_checkpoint)
        return keras.models.load_model(latest_checkpoint)
    print("Creating a new model")
    return get_compiled_model()
```

### os.listdir
```python
input_dir = "images/"
target_dir = "annotations/trimaps/"
img_size = (160, 160)
num_classes = 3
batch_size = 32

input_img_paths = sorted(
    [
        os.path.join(input_dir, fname)
        for fname in os.listdir(input_dir)
        if fname.endswith(".jpg")
    ]
)
target_img_paths = sorted(
    [
        os.path.join(target_dir, fname)
        for fname in os.listdir(target_dir)
        if fname.endswith(".png") and not fname.startswith(".")
    ]
)

print("Number of samples:", len(input_img_paths))

for input_path, target_path in zip(input_img_paths[:10], target_img_paths[:10]):
    print(input_path, "|", target_path)
```


## zipfile
```python
# Unzip data in the newly created directory.
with zipfile.ZipFile("CT-0.zip", "r") as z_fp:
    z_fp.extractall("./MosMedData/")

with zipfile.ZipFile("CT-23.zip", "r") as z_fp:
    z_fp.extractall("./MosMedData/")
```

```python
  _, zipped_file = tempfile.mkstemp('.zip')
  with zipfile.ZipFile(zipped_file, 'w', compression=zipfile.ZIP_DEFLATED) as f:
    f.write(file)

  return os.path.getsize(zipped_file)
```

## numpy 
### np.array
```python
# Read and process the scans.
# Each scan is resized across height, width, and depth and rescaled.
abnormal_scans = np.array([process_scan(path) for path in abnormal_scan_paths])
normal_scans = np.array([process_scan(path) for path in normal_scan_paths])

# For the CT scans having presence of viral pneumonia
# assign 1, for the normal ones assign 0.
abnormal_labels = np.array([1 for _ in range(len(abnormal_scans))])
normal_labels = np.array([0 for _ in range(len(normal_scans))])
```
### np.arrange
### np.argmax
### np.bool
### np.bincount
### np.clip
### np.concatenate
```python
# Split data in the ratio 70-30 for training and validation.
x_train = np.concatenate((abnormal_scans[:70], normal_scans[:70]), axis=0)
y_train = np.concatenate((abnormal_labels[:70], normal_labels[:70]), axis=0)
x_val = np.concatenate((abnormal_scans[70:], normal_scans[70:]), axis=0)
y_val = np.concatenate((abnormal_labels[70:], normal_labels[70:]), axis=0)
print(
    "Number of samples in train and validation are %d and %d."
    % (x_train.shape[0], x_val.shape[0])
)
```
### np.cumsum
### np.copy
### np.cos
### np.ceil
### np.count_nonzero
### np.concolve
### np.expand_dims
### np.eye
### np.empty
### np.exp
### np.einsum
### np.finfo
### np.float32
### np.fromstring
### np.hstack
### np.hamming
### np.int64
### np.int32
### np.inf
### np.load
### np.linspace
### np.log
### np.log2
### np.loadtxt
### np.less
### np.mean
### np.min
### np.max
### np.newaxis
### np.nan_to_num
### np.ones
### np.pi
### np.prod
### np.power
### np.pad
### np.reshape
```python
data = np.rot90(np.array(data))
data = np.transpose(data)
data = np.reshape(data, (num_rows, num_columns, width, height))
```
### np.ravel
### np.round
### np.random.choice
### np.random.randint
### np.random.shuffle
### np.random.normal
### np.random.randn
### np.random.rand
### np.random.random
### np.random.seed
### np.random.uniform
### np.random.multinomial
### np.random.RandomState
### np.random.permutation
### np.random.random_sample
### np.sum
### np.std
### np.squeeze
### np.swapaxes
### np.sqrt
### np.sign
### np.sort
### np.sin
### np.stack
### np.shape
### np.square
### np.testing.assert_allclose
### np.triu
### np.uint8
### np.unique
### np.var
### np.where
### np.zeros







## time
```python
import time

epochs = 2
for epoch in range(epochs):
    print("\nStart of epoch %d" % (epoch,))
    start_time = time.time()

    # Iterate over the batches of the dataset.
    for step, (x_batch_train, y_batch_train) in enumerate(train_dataset):
        loss_value = train_step(x_batch_train, y_batch_train)

        # Log every 200 batches.
        if step % 200 == 0:
            print(
                "Training loss (for one batch) at step %d: %.4f"
                % (step, float(loss_value))
            )
            print("Seen so far: %d samples" % ((step + 1) * 64))

    # Display metrics at the end of each epoch.
    train_acc = train_acc_metric.result()
    print("Training acc over epoch: %.4f" % (float(train_acc),))

    # Reset training metrics at the end of each epoch
    train_acc_metric.reset_states()

    # Run a validation loop at the end of each epoch.
    for x_batch_val, y_batch_val in val_dataset:
        test_step(x_batch_val, y_batch_val)

    val_acc = val_acc_metric.result()
    val_acc_metric.reset_states()
    print("Validation acc: %.4f" % (float(val_acc),))
    print("Time taken: %.2fs" % (time.time() - start_time))
```


## datetime

```python
gcp_bucket = "keras-examples"

checkpoint_path = os.path.join("gs://", gcp_bucket, "mnist_example", "save_at_{epoch}")

tensorboard_path = os.path.join(  # Timestamp included to enable timeseries graphs
    "gs://", gcp_bucket, "logs", datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
)
```

## pandas

### pd.read_csv
```python
data_url = "https://archive.ics.uci.edu/ml/machine-learning-databases/census-income-mld/census-income.data.gz"
data = pd.read_csv(data_url, header=None, names=CSV_HEADER)

test_data_url = "https://archive.ics.uci.edu/ml/machine-learning-databases/census-income-mld/census-income.test.gz"
test_data = pd.read_csv(test_data_url, header=None, names=CSV_HEADER)
```

```python
citations = pd.read_csv(
    os.path.join(data_dir, "cora.cites"),
    sep="\t",
    header=None,
    names=["target", "source"],
)
print("Citations shape:", citations.shape)
```
### pd.concat
```python
train_data, test_data = [], []

for _, group_data in papers.groupby("subject"):
    # Select around 50% of the dataset for training.
    random_selection = np.random.rand(len(group_data.index)) <= 0.5
    train_data.append(group_data[random_selection])
    test_data.append(group_data[~random_selection])

train_data = pd.concat(train_data).sample(frac=1)
test_data = pd.concat(test_data).sample(frac=1)

print("Train data shape:", train_data.shape)
print("Test data shape:", test_data.shape)
```
### pd.DataFrame
```python
def get_data_from_text_files(folder_name):

    pos_files = glob.glob("aclImdb/" + folder_name + "/pos/*.txt")
    pos_texts = get_text_list_from_files(pos_files)
    neg_files = glob.glob("aclImdb/" + folder_name + "/neg/*.txt")
    neg_texts = get_text_list_from_files(neg_files)
    df = pd.DataFrame(
        {
            "review": pos_texts + neg_texts,
            "sentiment": [0] * len(pos_texts) + [1] * len(neg_texts),
        }
    )
    df = df.sample(len(df)).reset_index(drop=True)
    return df
```

```python
selected_features = [feature_keys[i] for i in [0, 1, 5, 7, 8, 10, 11]]
features = df[selected_features]
features.index = df[date_time_key]
features.head()

features = normalize(features.values, train_split)
features = pd.DataFrame(features)
features.head()
```

## keras_tuner

### keras_tuner.applications.HyperResNet


```python
hypermodel = HyperResNet(input_shape=(28, 28, 1), classes=10)

tuner = RandomSearch(
    hypermodel,
    objective="val_accuracy",
    max_trials=3,
    overwrite=True,
    directory="my_dir",
    project_name="helloworld",
)

tuner.search(
    x_train[:100], y_train[:100], epochs=1, validation_data=(x_val[:100], y_val[:100])
)
```

### keras_tuner.applications.HyperXception
```python
from keras_tuner import HyperParameters
from keras_tuner.applications import HyperXception

hypermodel = HyperXception(input_shape=(28, 28, 1), classes=10)

hp = HyperParameters()

# This will override the `learning_rate` parameter with your
# own selection of choices
hp.Choice("learning_rate", values=[1e-2, 1e-3, 1e-4])

tuner = RandomSearch(
    hypermodel,
    hyperparameters=hp,
    # `tune_new_entries=False` prevents unlisted parameters from being tuned
    tune_new_entries=False,
    objective="val_accuracy",
    max_trials=3,
    overwrite=True,
    directory="my_dir",
    project_name="helloworld",
)

tuner.search(
    x_train[:100], y_train[:100], epochs=1, validation_data=(x_val[:100], y_val[:100])
)
```
### kt.HyperParameters
```python
# Initialize the `HyperParameters` and set the values.
hp = kt.HyperParameters()
hp.values["model_type"] = "cnn"
# Build the model using the `HyperParameters`.
model = build_model(hp)
# Test if the model runs with our data.
model(x_train[:100])
# Print a summary of the model.
model.summary()

# Do the same for MLP model.
hp.values["model_type"] = "mlp"
model = build_model(hp)
model(x_train[:100])
model.summary()
```

### kt.Tuner
### kt.oracles.BayesianOptimization
```python
tuner = MyTuner(
    oracle=kt.oracles.BayesianOptimization(
        objective=kt.Objective("loss", "min"), max_trials=2
    ),
    hypermodel=build_model,
    directory="results",
    project_name="mnist_custom_training",
)

(x_train, y_train), (x_test, y_test) = tf.keras.datasets.mnist.load_data()

# Reshape the images to have the channel dimension.
x_train = x_train.reshape(x_train.shape + (1,))[:1000]
y_train = y_train.astype(np.int64)[:1000]

mnist_train = tf.data.Dataset.from_tensor_slices((x_train, y_train))

tuner.search(train_ds=mnist_train)

best_hps = tuner.get_best_hyperparameters()[0]
print(best_hps.values)

best_model = tuner.get_best_models()[0]
```
### kt.Objective
```python
tuner = MyTuner(
    oracle=kt.oracles.BayesianOptimization(
        objective=kt.Objective("loss", "min"), max_trials=2
    ),
    hypermodel=build_model,
    directory="results",
    project_name="mnist_custom_training",
)
```
### kt.Hyperband
```python
tuner = kt.Hyperband(
    hypermodel=build_model,
    objective="val_accuracy",
    max_epochs=2,
    factor=3,
    hyperband_iterations=1,
    distribution_strategy=tf.distribute.MirroredStrategy(),
    directory="results_dir",
    project_name="mnist",
    overwrite=True,
)

(x_train, y_train), (x_test, y_test) = tf.keras.datasets.mnist.load_data()

# Reshape the images to have the channel dimension.
x_train = (x_train.reshape(x_train.shape + (1,)) / 255.0)[:1000]
y_train = y_train.astype(np.int64)[:1000]
x_test = (x_test.reshape(x_test.shape + (1,)) / 255.0)[:100]
y_test = y_test.astype(np.int64)[:100]

tuner.search(
    x_train,
    y_train,
    steps_per_epoch=600,
    validation_data=(x_test, y_test),
    validation_steps=100,
    callbacks=[tf.keras.callbacks.EarlyStopping("val_accuracy")],
)
```

### kt.RandomSearch
```python
tuner = kt.RandomSearch(
    build_model,
    max_trials=10,
    # Do not resume the previous search in the same directory.
    overwrite=True,
    objective="val_accuracy",
    # Set a directory to store the intermediate results.
    directory="/tmp/tb",
)
```
### kt.HyperModel
```python
hypermodel = HyperXception(input_shape=(28, 28, 1), classes=10)

hp = HyperParameters()
hp.Fixed("learning_rate", value=1e-4)

tuner = RandomSearch(
    hypermodel,
    hyperparameters=hp,
    tune_new_entries=True,
    objective="val_accuracy",
    max_trials=3,
    overwrite=True,
    directory="my_dir",
    project_name="helloworld",
)

tuner.search(
    x_train[:100], y_train[:100], epochs=1, validation_data=(x_val[:100], y_val[:100])
)
```


```python
class MyTuner(kt.Tuner):
    def run_trial(self, trial, train_ds):
        hp = trial.hyperparameters

        # Hyperparameters can be added anywhere inside `run_trial`.
        # When the first trial is run, they will take on their default values.
        # Afterwards, they will be tuned by the `Oracle`.
        train_ds = train_ds.batch(hp.Int("batch_size", 32, 128, step=32, default=64))

        model = self.hypermodel.build(trial.hyperparameters)
        lr = hp.Float("learning_rate", 1e-4, 1e-2, sampling="log", default=1e-3)
        optimizer = tf.keras.optimizers.Adam(lr)
        epoch_loss_metric = tf.keras.metrics.Mean()
        loss_fn = tf.keras.losses.SparseCategoricalCrossentropy()

        # @tf.function
        def run_train_step(data):
            images = tf.dtypes.cast(data[0], "float32") / 255.0
            labels = data[1]
            with tf.GradientTape() as tape:
                logits = model(images)
                loss = loss_fn(labels, logits)
                # Add any regularization losses.
                if model.losses:
                    loss += tf.math.add_n(model.losses)
            gradients = tape.gradient(loss, model.trainable_variables)
            optimizer.apply_gradients(zip(gradients, model.trainable_variables))
            epoch_loss_metric.update_state(loss)
            return loss

        # `self.on_epoch_end` reports results to the `Oracle` and saves the
        # current state of the Model. The other hooks called here only log values
        # for display but can also be overridden. For use cases where there is no
        # natural concept of epoch, you do not have to call any of these hooks. In
        # this case you should instead call `self.oracle.update_trial` and
        # `self.oracle.save_model` manually.
        for epoch in range(2):
            print("Epoch: {}".format(epoch))

            self.on_epoch_begin(trial, model, epoch, logs={})
            for batch, data in enumerate(train_ds):
                self.on_batch_begin(trial, model, batch, logs={})
                batch_loss = float(run_train_step(data))
                self.on_batch_end(trial, model, batch, logs={"loss": batch_loss})

                if batch % 100 == 0:
                    loss = epoch_loss_metric.result().numpy()
                    print("Batch: {}, Average Loss: {}".format(batch, loss))

            epoch_loss = epoch_loss_metric.result().numpy()
            self.on_epoch_end(trial, model, epoch, logs={"loss": epoch_loss})
            epoch_loss_metric.reset_states()


tuner = MyTuner(
    oracle=kt.oracles.BayesianOptimization(
        objective=kt.Objective("loss", "min"), max_trials=2
    ),
    hypermodel=build_model,
    directory="results",
    project_name="mnist_custom_training",
)

(x_train, y_train), (x_test, y_test) = tf.keras.datasets.mnist.load_data()

# Reshape the images to have the channel dimension.
x_train = x_train.reshape(x_train.shape + (1,))[:1000]
y_train = y_train.astype(np.int64)[:1000]

mnist_train = tf.data.Dataset.from_tensor_slices((x_train, y_train))

tuner.search(train_ds=mnist_train)

best_hps = tuner.get_best_hyperparameters()[0]
print(best_hps.values)

best_model = tuner.get_best_models()[0]
```





## keras.engine
### keras.engine.get_source_inputs


#### keras.utils.layer_utils
get_source_inputs

#### keras.utils.data_utils
get_file

#### keras.utils.conv_utils

#### keras.preprocessing.image
load_img
img_to_array
array_to_img
image_dataset_from_directory


### keras.applications
MobileNetV2
keras_modules_injection

#### keras.applications.resnet50
preprocess_input

#### keras.layers.experimental
RandomFourierFeatures

## deeplab
DeepLabV3Plus

## deeplabv3plus

### deeplabv3plus.datasets
GenericDataLoader

### deeplabv3plus.model
DeeplabV3Plus

### deeplabv3plus.train
Trainer

### deeplabv3plus.inference
read_image
infer

### deeplabv3plus.utils

## tensorflow_datasets

```python
tfds.disable_progress_bar()

# SVHN
svhn_train, svhn_test = tfds.load(
    "svhn_cropped", split=["train", "test"], as_supervised=True
)
```

## tensorflow_docs
## tensorflow_hub
## tensorflow_text
## tensorflow_cloud

install via：

```shell
pip install tensorflow-cloud
```
TensorFlow Cloud takes all the code from its local execution environment (this
notebook), wraps it up, and sends it to Google Cloud for execution. (That's why
the `if` and `if not` `tfc.remote` wrappers are important.)

```python
import tensorflow_cloud as tfc
if not tfc.remote():
if tfc.remote():
```


```python
# If you are using a custom image you can install modules via requirements 
# txt file.
# 如果您使用的是自定义映像，则可以通过requirements txt文件安装模块。
with open("requirements.txt", "w") as f:
    f.write("tensorflow-cloud\n")

# Optional: Some recommended base images. If you provide none the system
# will choose one for you.
# Optional: 一些推荐的基本图像。如果您没有提供，系统将为您选择一个。
TF_GPU_IMAGE = "gcr.io/deeplearning-platform-release/tf2-cpu.2-5"
TF_CPU_IMAGE = "gcr.io/deeplearning-platform-release/tf2-gpu.2-5"

# Submit a single node training job using GPU.
# 使用GPU提交单节点培训作业。
tfc.run(
    distribution_strategy="auto",
    requirements_txt="requirements.txt",
    docker_config=tfc.DockerConfig(
        parent_image=TF_GPU_IMAGE, image_build_bucket=GCS_BUCKET
    ),
    chief_config=tfc.COMMON_MACHINE_CONFIGS["K80_1X"],
    job_labels={"job": JOB_NAME},
```
```python
tfc.run(
    docker_image_bucket_name=gcp_bucket,
    entry_point="train_model.py",
    requirements="requirements.txt"
)
```
```python
tfc.run(
    docker_image_bucket_name=gcp_bucket,
    chief_config=tfc.COMMON_MACHINE_CONFIGS['CPU'],
    worker_count=2,
    worker_config=tfc.COMMON_MACHINE_CONFIGS['T4_4X']
)
```
```python
tfc.run(
    docker_image_bucket_name=gcp_bucket,
    chief_config=tfc.COMMON_MACHINE_CONFIGS["CPU"],
    worker_count=1,
    worker_config=tfc.COMMON_MACHINE_CONFIGS["TPU"]
)
```

```python
(x_train, y_train), (x_test, y_test) = keras.datasets.mnist.load_data()
 
mirrored_strategy = tf.distribute.MirroredStrategy()
with mirrored_strategy.scope():
  model = create_model()
 
if tfc.remote():
    epochs = 100
    batch_size = 128
else:
    epochs = 10
    batch_size = 64
    callbacks = None
 
model.fit(
    x_train, y_train, epochs=epochs, callbacks=callbacks, batch_size=batch_size
)
 
tfc.run(
    docker_image_bucket_name=gcp_bucket,
    chief_config=tfc.COMMON_MACHINE_CONFIGS['CPU'],
    worker_count=2,
    worker_config=tfc.COMMON_MACHINE_CONFIGS['T4_4X'],
    distribution_strategy=None
)
```

```python
tfc.run(
    docker_image_bucket_name=gcp_bucket,
    base_docker_image="tensorflow/tensorflow:2.1.0-gpu"
)
```

```python
job_labels = {"job": "mnist-example", "team": "keras-io", "user": "jonah"}
 
tfc.run(
    docker_image_bucket_name=gcp_bucket,
    job_labels=job_labels,
    stream_logs=True
)
```

```python
if tfc.remote():
    epochs = 100
    callbacks = callbacks
    batch_size = 128
else:
    epochs = 5
    batch_size = 64
    callbacks = None

model.fit(x_train, y_train, epochs=epochs, callbacks=callbacks, batch_size=batch_size)

save_path = os.path.join("gs://", gcp_bucket, "mnist_example")

if tfc.remote():
    model.save(save_path)

# docs_infra: no_execute
tfc.run(docker_image_bucket_name=gcp_bucket)
```

### tensorflow_docs.vis
embed
一个简单的python库，可以从中获取任何url并返回最有意义的数据
```python
fake_images *= 255.0
converted_images = fake_images.astype(np.uint8)
converted_images = tf.image.resize(converted_images, (96, 96)).numpy().astype(np.uint8)
imageio.mimsave("animation.gif", converted_images, fps=1)
embed.embed_file("animation.gif")
```

## tensorflow_probability
```python
# Define the prior weight distribution as Normal of mean=0 and stddev=1.
# Note that, in this example, the we prior distribution is not trainable,
# as we fix its parameters.
def prior(kernel_size, bias_size, dtype=None):
    n = kernel_size + bias_size
    prior_model = keras.Sequential(
        [
            tfp.layers.DistributionLambda(
                lambda t: tfp.distributions.MultivariateNormalDiag(
                    loc=tf.zeros(n), scale_diag=tf.ones(n)
                )
            )
        ]
    )
    return prior_model

# Define variational posterior weight distribution as multivariate Gaussian.
# Note that the learnable parameters for this distribution are the means,
# variances, and covariances.
def posterior(kernel_size, bias_size, dtype=None):
    n = kernel_size + bias_size
    posterior_model = keras.Sequential(
        [
            tfp.layers.VariableLayer(
                tfp.layers.MultivariateNormalTriL.params_size(n), dtype=dtype
            ),
            tfp.layers.MultivariateNormalTriL(n),
        ]
    )
    return posterior_model


def create_bnn_model(train_size):
    inputs = create_model_inputs()
    features = keras.layers.concatenate(list(inputs.values()))
    features = layers.BatchNormalization()(features)

    # Create hidden layers with weight uncertainty using the DenseVariational layer.
    for units in hidden_units:
        features = tfp.layers.DenseVariational(
            units=units,
            make_prior_fn=prior,
            make_posterior_fn=posterior,
            kl_weight=1 / train_size,
            activation="sigmoid",
        )(features)

    # The output is deterministic: a single point estimate.
    outputs = layers.Dense(units=1)(features)
    model = keras.Model(inputs=inputs, outputs=outputs)
    return model

def create_probablistic_bnn_model(train_size):
    inputs = create_model_inputs()
    features = keras.layers.concatenate(list(inputs.values()))
    features = layers.BatchNormalization()(features)

    # Create hidden layers with weight uncertainty using the DenseVariational layer.
    for units in hidden_units:
        features = tfp.layers.DenseVariational(
            units=units,
            make_prior_fn=prior,
            make_posterior_fn=posterior,
            kl_weight=1 / train_size,
            activation="sigmoid",
        )(features)

    # Create a probabilisticå output (Normal distribution), and use the `Dense` layer
    # to produce the parameters of the distribution.
    # We set units=2 to learn both the mean and the variance of the Normal distribution.
    distribution_params = layers.Dense(units=2)(features)
    outputs = tfp.layers.IndependentNormal(1)(distribution_params)

    model = keras.Model(inputs=inputs, outputs=outputs)
    return model
```


## tensorflow_addons
```python
# Define the callbacks.
reduce_lr = tf.keras.callbacks.ReduceLROnPlateau(patience=3)
early_stopping = tf.keras.callbacks.EarlyStopping(
    patience=10, restore_best_weights=True
)

# Initialize SWA from tf-hub.
SWA = tfa.optimizers.SWA

# Compile and train the teacher model.
teacher_model = get_training_model()
teacher_model.load_weights("initial_teacher_model.h5")
teacher_model.compile(
    # Notice that we are wrapping our optimizer within SWA
    optimizer=SWA(tf.keras.optimizers.Adam()),
    loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
    metrics=["accuracy"],
)
history = teacher_model.fit(
    train_clean_ds,
    epochs=EPOCHS,
    validation_data=validation_ds,
    callbacks=[reduce_lr, early_stopping],
)

# Evaluate the teacher model on the test set.
_, acc = teacher_model.evaluate(test_ds, verbose=0)
print(f"Test accuracy: {acc*100}%")
```

```python
def run_experiment(model):
    optimizer = tfa.optimizers.AdamW(learning_rate=0.001, weight_decay=0.0001)

    model.compile(
        optimizer=optimizer,
        loss=keras.losses.CategoricalCrossentropy(
            from_logits=True, label_smoothing=0.1
        ),
        metrics=[
            keras.metrics.CategoricalAccuracy(name="accuracy"),
            keras.metrics.TopKCategoricalAccuracy(5, name="top-5-accuracy"),
        ],
    )

    checkpoint_filepath = "/tmp/checkpoint"
    checkpoint_callback = keras.callbacks.ModelCheckpoint(
        checkpoint_filepath,
        monitor="val_accuracy",
        save_best_only=True,
        save_weights_only=True,
    )

    history = model.fit(
        x=x_train,
        y=y_train,
        batch_size=batch_size,
        epochs=num_epochs,
        validation_split=0.1,
        callbacks=[checkpoint_callback],
    )

    model.load_weights(checkpoint_filepath)
    _, accuracy, top_5_accuracy = model.evaluate(x_test, y_test)
    print(f"Test accuracy: {round(accuracy * 100, 2)}%")
    print(f"Test top 5 accuracy: {round(top_5_accuracy * 100, 2)}%")

    return history


cct_model = create_cct_model()
history = run_experiment(cct_model)
```

### tensorflow_addons.layers
InstanceNormaliztion

### tensorflow.experimental.numpy

## tensorflow_model_optimization
```python
# Define the model.
base_model = setup_model()
base_model.load_weights(pretrained_weights) # optional but recommended for model accuracy
model_for_pruning = tfmot.sparsity.keras.prune_low_magnitude(base_model)

log_dir = tempfile.mkdtemp()
callbacks = [
    tfmot.sparsity.keras.UpdatePruningStep(),
    # Log sparsity and other metrics in Tensorboard.
    tfmot.sparsity.keras.PruningSummaries(log_dir=log_dir)
]

model_for_pruning.compile(
      loss=tf.keras.losses.categorical_crossentropy,
      optimizer='adam',
      metrics=['accuracy']
)

model_for_pruning.fit(
    x_train,
    y_train,
    callbacks=callbacks,
    epochs=2,
)

#docs_infra: no_execute
%tensorboard --logdir={log_dir}
```

```python
base_model = setup_model()
base_model.load_weights(pretrained_weights) # optional but recommended.

model_for_pruning = tfmot.sparsity.keras.prune_low_magnitude(base_model)

model_for_pruning.summary()
```
```python
# Define the model.
base_model = setup_model()
base_model.load_weights(pretrained_weights) # optional but recommended for model accuracy
model_for_pruning = tfmot.sparsity.keras.prune_low_magnitude(base_model)

# Typically you train the model here.

model_for_export = tfmot.sparsity.keras.strip_pruning(model_for_pruning)

print("final model")
model_for_export.summary()

print("\n")
print("Size of gzipped pruned model without stripping: %.2f bytes" % (get_gzipped_model_size(model_for_pruning)))
print("Size of gzipped pruned model with stripping: %.2f bytes" % (get_gzipped_model_size(model_for_export)))
```
```python
base_model = setup_model()

# For using intrinsics on a CPU with 128-bit registers, together with 8-bit
# quantized weights, a 1x16 block size is nice because the block perfectly
# fits into the register.
pruning_params = {'block_size': [1, 16]}
model_for_pruning = tfmot.sparsity.keras.prune_low_magnitude(base_model, **pruning_params)

model_for_pruning.summary()
```

```python
cluster_weights = tfmot.clustering.keras.cluster_weights
CentroidInitialization = tfmot.clustering.keras.CentroidInitialization

clustering_params = {
  'number_of_clusters': 3,
  'cluster_centroids_init': CentroidInitialization.DENSITY_BASED
}

model = setup_model()
model.load_weights(pretrained_weights)

clustered_model = cluster_weights(model, **clustering_params)

clustered_model.summary()

#----------------------------------------------------------------------
#----------------------------------------------------------------------

# Define the model.
base_model = setup_model()
base_model.load_weights(pretrained_weights)
clustered_model = cluster_weights(base_model, **clustering_params)

# Save or checkpoint the model.
_, keras_model_file = tempfile.mkstemp('.h5')
clustered_model.save(keras_model_file, include_optimizer=True)

# `cluster_scope` is needed for deserializing HDF5 models.
with tfmot.clustering.keras.cluster_scope():
  loaded_model = tf.keras.models.load_model(keras_model_file)

loaded_model.summary()

#----------------------------------------------------------------------
#----------------------------------------------------------------------

model = setup_model()
clustered_model = cluster_weights(model, **clustering_params)

clustered_model.compile(
    loss=tf.keras.losses.categorical_crossentropy,
    optimizer='adam',
    metrics=['accuracy']
)

clustered_model.fit(
    x_train,
    y_train
)

final_model = tfmot.clustering.keras.strip_clustering(clustered_model)

print("final model")
final_model.summary()
```

## official.vision.image_classification.augment
RandAugment
```python
# Initialize `RandAugment` object with 2 layers of
# augmentation transforms and strength of 9.
augmenter = RandAugment(num_layers=2, magnitude=9)
```

```python
# Initialize `RandAugment` object with 2 layers of
# augmentation transforms and strength of 5.
augmenter = RandAugment(num_layers=2, magnitude=5)


def weak_augment(image, source=True):
    if image.dtype != tf.float32:
        image = tf.cast(image, tf.float32)

    # MNIST images are grayscale, this is why we first convert them to
    # RGB images.
    if source:
        image = tf.image.resize_with_pad(image, RESIZE_TO, RESIZE_TO)
        image = tf.tile(image, [1, 1, 3])
    image = tf.image.random_flip_left_right(image)
    image = tf.image.random_crop(image, (RESIZE_TO, RESIZE_TO, 3))
    return image


def strong_augment(image, source=True):
    if image.dtype != tf.float32:
        image = tf.cast(image, tf.float32)

    if source:
        image = tf.image.resize_with_pad(image, RESIZE_TO, RESIZE_TO)
        image = tf.tile(image, [1, 1, 3])
    image = augmenter.distort(image)
    return image
```

## tf.math
### tf.math.divide_no_nan
### tf.math.argmax
### tf.math.maximum
### tf.math.minimum
### tf.math.abs
### tf.math.add_n
### tf.math.cumsum
### tf.math.ceil
### tf.math.not_equal
### tf.math.equal
### tf.math.exp
### tf.math.floordiv
### tf.math.floormod
### tf.math.less
### tf.math.log
### tf.math.logical_and
### tf.math.logical_not
### tf.math.l2_normalize
### tf.math.minimum
### tf.math.pow
### tf.math.reduce_mean
### tf.math.reduce_max
### tf.math.reduce_sum
### tf.math.reduce_std
### tf.math.reduce_prod
### tf.math.sqrt
### tf.math.square
### tf.math.top_k
### tf.math.unsorted_segment_sum

## tensorflow







```python
tf.random.set_seed(42)
```

```python
AUTO = tf.data.AUTOTUNE
```


```python
(
    (mnist_x_train, mnist_y_train),
    (mnist_x_test, mnist_y_test),
) = keras.datasets.mnist.load_data()

# Add a channel dimension
mnist_x_train = tf.expand_dims(mnist_x_train, -1)
mnist_x_test = tf.expand_dims(mnist_x_test, -1)

# Convert the labels to one-hot encoded vectors
mnist_y_train = tf.one_hot(mnist_y_train, 10).numpy()
```

### tensorflow.data.Dataset
```python
#  Utility function for preprocessing the source test set.
def prepare_test_ds_source(image, label):
    image = tf.image.resize_with_pad(image, RESIZE_TO, RESIZE_TO)
    image = tf.tile(image, [1, 1, 3])
    return image, label


source_test_ds = tf.data.Dataset.from_tensor_slices((mnist_x_test, mnist_y_test))
source_test_ds = (
    source_test_ds.map(prepare_test_ds_source, num_parallel_calls=AUTO)
    .batch(TARGET_BATCH_SIZE)
    .prefetch(AUTO)
)

# Evaluation on the source test set.
_, accuracy = adamatch_trained_model.evaluate(source_test_ds)
print(f"Accuracy on source test set: {accuracy * 100:.2f}%")
```

```python
# Define data loaders.
train_loader = tf.data.Dataset.from_tensor_slices((x_train, y_train))
validation_loader = tf.data.Dataset.from_tensor_slices((x_val, y_val))

batch_size = 2
# Augment the on the fly during training.
train_dataset = (
    train_loader.shuffle(len(x_train))
    .map(train_preprocessing)
    .batch(batch_size)
    .prefetch(2)
)
# Only rescale.
validation_dataset = (
    validation_loader.shuffle(len(x_val))
    .map(validation_preprocessing)
    .batch(batch_size)
    .prefetch(2)
)
```

```python
def create_individual_ds(ds, aug_func, source=True):
    if source:
        batch_size = SOURCE_BATCH_SIZE
    else:
        # During training 3x more target unlabeled samples are shown
        # to the model in AdaMatch (Section 3.2 of the paper).
        batch_size = TARGET_BATCH_SIZE
    ds = ds.shuffle(batch_size * 10, seed=42)

    if source:
        ds = ds.map(lambda x, y: (aug_func(x), y), num_parallel_calls=AUTO)
    else:
        ds = ds.map(lambda x, y: (aug_func(x, False), y), num_parallel_calls=AUTO)

    ds = ds.batch(batch_size).prefetch(AUTO)
    return ds

source_ds = tf.data.Dataset.from_tensor_slices((mnist_x_train, mnist_y_train))
source_ds_w = create_individual_ds(source_ds, weak_augment)
source_ds_s = create_individual_ds(source_ds, strong_augment)
final_source_ds = tf.data.Dataset.zip((source_ds_w, source_ds_s))

target_ds_w = create_individual_ds(svhn_train, weak_augment, source=False)
target_ds_s = create_individual_ds(svhn_train, strong_augment, source=False)
final_target_ds = tf.data.Dataset.zip((target_ds_w, target_ds_s))
```

```python
# Compile the AdaMatch model to yield accuracy.
adamatch_trained_model = adamatch_trainer.model
adamatch_trained_model.compile(metrics=keras.metrics.SparseCategoricalAccuracy())

# Score on the target test set.
svhn_test = svhn_test.batch(TARGET_BATCH_SIZE).prefetch(AUTO)
_, accuracy = adamatch_trained_model.evaluate(svhn_test)
print(f"Accuracy on target test set: {accuracy * 100:.2f}%")
```



### tensorflow.util.tf_export
keras_export



## nibabel
```python
import nibabel as nib
# NiBabel包是可以对常见的医学和神经影像文件格式进行读写

def read_nifti_file(filepath):
    """Read and load volume"""
    # Read file
    scan = nib.load(filepath)
    # Get raw data
    scan = scan.get_fdata()
    return scan
```

## scipy
### scipy.ndimage 
```python
def resize_volume(img):
    """Resize across z-axis"""
    # Set the desired depth
    desired_depth = 64
    desired_width = 128
    desired_height = 128
    # Get current depth
    current_depth = img.shape[-1]
    current_width = img.shape[0]
    current_height = img.shape[1]
    # Compute depth factor
    depth = current_depth / desired_depth
    width = current_width / desired_width
    height = current_height / desired_height
    depth_factor = 1 / depth
    width_factor = 1 / width
    height_factor = 1 / height
    # Rotate
    img = ndimage.rotate(img, 90, reshape=False)
    # Resize across z-axis
    img = ndimage.zoom(img, (width_factor, height_factor, depth_factor), order=1)
    return img
```

```python
@tf.function
def rotate(volume):
    """Rotate the volume by a few degrees"""

    def scipy_rotate(volume):
        # define some rotation angles
        angles = [-20, -10, -5, 5, 10, 20]
        # pick angles at random
        angle = random.choice(angles)
        # rotate volume
        volume = ndimage.rotate(volume, angle, reshape=False)
        volume[volume < 0] = 0
        volume[volume > 1] = 1
        return volume

    augmented_volume = tf.numpy_function(scipy_rotate, [volume], tf.float32)
    return augmented_volume
```

### scipy.io
loadmat

### scipy.signal

## random
```python
@tf.function
def rotate(volume):
    """Rotate the volume by a few degrees"""

    def scipy_rotate(volume):
        # define some rotation angles
        angles = [-20, -10, -5, 5, 10, 20]
        # pick angles at random
        angle = random.choice(angles)
        # rotate volume
        volume = ndimage.rotate(volume, angle, reshape=False)
        volume[volume < 0] = 0
        volume[volume > 1] = 1
        return volume

    augmented_volume = tf.numpy_function(scipy_rotate, [volume], tf.float32)
    return augmented_volume
```

## random.shuffle
## matplotlib
### matplotlib.pyplot 
```python
plt.imshow(np.squeeze(image[:, :, 30]), cmap="gray")
```

```python
    f, axarr = plt.subplots(
        rows_data,
        columns_data,
        figsize=(fig_width, fig_height),
        gridspec_kw={"height_ratios": heights},
    )
    for i in range(rows_data):
        for j in range(columns_data):
            axarr[i, j].imshow(data[i][j], cmap="gray")
            axarr[i, j].axis("off")
    plt.subplots_adjust(wspace=0, hspace=0, left=0, right=1, bottom=0, top=1)
    plt.show()
```

```python
fig, ax = plt.subplots(1, 2, figsize=(20, 3))
ax = ax.ravel()

for i, metric in enumerate(["acc", "loss"]):
    ax[i].plot(model.history.history[metric])
    ax[i].plot(model.history.history["val_" + metric])
    ax[i].set_title("Model {}".format(metric))
    ax[i].set_xlabel("epochs")
    ax[i].set_ylabel(metric)
    ax[i].legend(["train", "val"])
```
### matplotlib.gridspec ★
### matplotlib.image
### matplotlib.cm

## io
### io.BytesIO ★
## tarfile ★
## tempfile 
```python
def setup_pretrained_weights():
  model = setup_model()

  model.compile(
      loss=tf.keras.losses.categorical_crossentropy,
      optimizer='adam',
      metrics=['accuracy']
  )

  model.fit(x_train, y_train)

  _, pretrained_weights = tempfile.mkstemp('.tf')

  model.save_weights(pretrained_weights)

  return pretrained_weights
```

```python
# Define the model.
base_model = setup_model()
base_model.load_weights(pretrained_weights) # optional but recommended for model accuracy
model_for_pruning = tfmot.sparsity.keras.prune_low_magnitude(base_model)

log_dir = tempfile.mkdtemp()
callbacks = [
    tfmot.sparsity.keras.UpdatePruningStep(),
    # Log sparsity and other metrics in Tensorboard.
    tfmot.sparsity.keras.PruningSummaries(log_dir=log_dir)
]

model_for_pruning.compile(
      loss=tf.keras.losses.categorical_crossentropy,
      optimizer='adam',
      metrics=['accuracy']
)

model_for_pruning.fit(
    x_train,
    y_train,
    callbacks=callbacks,
    epochs=2,
)

#docs_infra: no_execute
%tensorboard --logdir={log_dir}
```

```python
# Define the model.
base_model = setup_model()
base_model.load_weights(pretrained_weights) # optional but recommended for model accuracy
model_for_pruning = tfmot.sparsity.keras.prune_low_magnitude(base_model)

# Boilerplate
loss = tf.keras.losses.categorical_crossentropy
optimizer = tf.keras.optimizers.Adam()
log_dir = tempfile.mkdtemp()
unused_arg = -1
epochs = 2
batches = 1 # example is hardcoded so that the number of batches cannot change.

# Non-boilerplate.
model_for_pruning.optimizer = optimizer
step_callback = tfmot.sparsity.keras.UpdatePruningStep()
step_callback.set_model(model_for_pruning)
log_callback = tfmot.sparsity.keras.PruningSummaries(log_dir=log_dir) # Log sparsity and other metrics in Tensorboard.
log_callback.set_model(model_for_pruning)

step_callback.on_train_begin() # run pruning callback
for _ in range(epochs):
  log_callback.on_epoch_begin(epoch=unused_arg) # run pruning callback
  for _ in range(batches):
    step_callback.on_train_batch_begin(batch=unused_arg) # run pruning callback

    with tf.GradientTape() as tape:
      logits = model_for_pruning(x_train, training=True)
      loss_value = loss(y_train, logits)
      grads = tape.gradient(loss_value, model_for_pruning.trainable_variables)
      optimizer.apply_gradients(zip(grads, model_for_pruning.trainable_variables))

  step_callback.on_epoch_end(batch=unused_arg) # run pruning callback

#docs_infra: no_execute
%tensorboard --logdir={log_dir}
```

```python
# Define the model.
base_model = setup_model()
base_model.load_weights(pretrained_weights) # optional but recommended for model accuracy
model_for_pruning = tfmot.sparsity.keras.prune_low_magnitude(base_model)

_, keras_model_file = tempfile.mkstemp('.h5')

# Checkpoint: saving the optimizer is necessary (include_optimizer=True is the default).
model_for_pruning.save(keras_model_file, include_optimizer=True)
```


```python
def save_model_weights(model):
  _, pretrained_weights = tempfile.mkstemp('.h5')
  model.save_weights(pretrained_weights)
  return pretrained_weights

# Save or checkpoint the model.
_, keras_model_file = tempfile.mkstemp('.h5')
clustered_model.save(keras_model_file, include_optimizer=True)
```

```python
_, keras_file = tempfile.mkstemp('.h5')
print('Saving model to: ', keras_file)
tf.keras.models.save_model(model, keras_file, include_optimizer=False)
```

```python
converter = tf.lite.TFLiteConverter.from_keras_model(final_model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_quant_model = converter.convert()

_, quantized_and_clustered_tflite_file = tempfile.mkstemp('.tflite')

with open(quantized_and_clustered_tflite_file, 'wb') as f:
  f.write(tflite_quant_model)

print('Saved quantized and clustered TFLite model to:', quantized_and_clustered_tflite_file)
print("Size of gzipped baseline Keras model: %.2f bytes" % (get_gzipped_model_size(keras_file)))
print("Size of gzipped clustered and quantized TFlite model: %.2f bytes" % (get_gzipped_model_size(quantized_and_clustered_tflite_file)))
```

## six.moves
### six.moves.urllib ★

### urlib.request
urlretrive

## pathlib
## PIL
### PIL.Image
### PIL.ImageOps

## cv2
## tqdm
## IPython

### IPython.display
Image
Audio
display

```python
# Display the videos.
print(" Truth\tPrediction")
for i in range(0, len(predicted_videos), 2):
    # Construct and display an `HBox` with the ground truth and prediction.
    box = HBox(
        [
            widgets.Image(value=predicted_videos[i]),
            widgets.Image(value=predicted_videos[i + 1]),
        ]
    )
    display(box)
```

## Ipywidgets
widgets
Layout
HBox


```python
# Display the videos.
print(" Truth\tPrediction")
for i in range(0, len(predicted_videos), 2):
    # Construct and display an `HBox` with the ground truth and prediction.
    box = HBox(
        [
            widgets.Image(value=predicted_videos[i]),
            widgets.Image(value=predicted_videos[i + 1]),
        ]
    )
    display(box)
```

#### sklearn.metrics.confusion_matrix
#### sklearn.datasets.make_moons

## datasets
load_dataset

## dataclasses
dataclass

## collections
Counter

## conlleval
evaluate

## tabulate
## warnings
## gym

## pathlib
Path

## collections
Counter
defaultdict

## imageio
```python
imageio.mimsave("animation.gif", converted_images, fps=1)
```

```python
# Construct a GIF from the frames.
with io.BytesIO() as gif:
      imageio.mimsave(gif, current_frames, "GIF", fps=5)
      predicted_videos.append(gif.getvalue())
```

## json
```python
annotation_file = os.path.join(annotations_dir, "instances_val2017.json")
```

## pprint
```python
pprint.pprint(annotations[60])
```
## glob
## argparse
### RawTextHelpFormatter
## wandb

## wandb.keras
WandbCallback

## pickle

## moviepy.editor
VideoFileClip
ImageSequenceClip

## shutil
## gdown
## networkx
## rdkit
### rdkit.Chem

#### rdkit.Chem.Draw
IPythonConsole
MolsToGridImage

### rdkit.RDLogger

## re
## string

## transformers
BertTokenizer
TFBertModel
BertConfig

## imgaug

### google.colab
auth



## kaggle_secrets
UserSecretsClient

## enperimental.preprocessing
InterLookup
Normalization
StringLookup
TextVectorization

## tokenizers
BertWordPieceTokenizer

## functools
partial

## csv

### skimage.io
imread

### skimage.transform
## re

## enum
Enum

### classification.augment
RndAugment

## imgaug
augmenters
