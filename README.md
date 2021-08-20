# TensorflowKeras
Study Notes of Yuting

# Section 1 - Packages
## os 

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



## zipfile
```python
# Unzip data in the newly created directory.
with zipfile.ZipFile("CT-0.zip", "r") as z_fp:
    z_fp.extractall("./MosMedData/")

with zipfile.ZipFile("CT-23.zip", "r") as z_fp:
    z_fp.extractall("./MosMedData/")
```


## numpy 

```python
# Read and process the scans.
# Each scan is resized across height, width, and depth and rescaled.
abnormal_scans = np.array([process_scan(path) for path in abnormal_scan_paths])
normal_scans = np.array([process_scan(path) for path in normal_scan_paths])

# For the CT scans having presence of viral pneumonia
# assign 1, for the normal ones assign 0.
abnormal_labels = np.array([1 for _ in range(len(abnormal_scans))])
normal_labels = np.array([0 for _ in range(len(normal_scans))])

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

```python
data = np.rot90(np.array(data))
data = np.transpose(data)
data = np.reshape(data, (num_rows, num_columns, width, height))
```

## math
## time
## datetime

```python
gcp_bucket = "keras-examples"

checkpoint_path = os.path.join("gs://", gcp_bucket, "mnist_example", "save_at_{epoch}")

tensorboard_path = os.path.join(  # Timestamp included to enable timeseries graphs
    "gs://", gcp_bucket, "logs", datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
)
```

## pandas

## keras_tuner
RandomSearch
HyperModel
HyperParameters

### keras_tuner.applications
HyperResNet
HyperXception

## keras

### keras.engine
Layer
InputSpec

### keras.engine.topology
get_source_inputs

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

## tensorflow_probability
## tensorflow_addons

### tensorflow_addons.layers
InstanceNormaliztion

### tensorflow.experimental.numpy

## tensorflow_model_optimization

## official.vision.image_classification.augment
RandAugment

## tensorflow

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

### tensorflow.util.tf_export
keras_export

### tensorflow.keras 
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
## tempfile ★
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

## Ipywidgets
widgets
Layout
HBox

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
## json
## pprint
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
