# TensorflowKeras
Study Notes of Yuting

# Section 1 - Packages
## os 

```python
TENSORBOARD_LOGS_DIR = os.path.join(GCS_BASE_PATH, "logs")
MODEL_CHECKPOINT_DIR = os.path.join(GCS_BASE_PATH, "checkpoints")
SAVED_MODEL_DIR = os.path.join(GCS_BASE_PATH, "saved_model")
```
## zipfile
## numpy 
## math
## time
## datetime
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
%load_ext tensorboard
%tensorboard --logdir $TENSORBOARD_LOGS_DIR
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

#### tensorflow.keras.callbacks
Callback
TensorBoard
ModelCheckpoint
EarlyStopping
LearningRateScheduler

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


#### tensorflow.keras.utils
layer_utils.get_source_inputs
data_utils.get_file

## nibabel
## scipy
### scipy.ndimage 

### scipy.io
loadmat

### scipy.signal

## random
## random.shuffle
## matplotlib
### matplotlib.pyplot ★
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
