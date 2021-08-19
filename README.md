# TensorflowKeras
Study Notes of Yuting

# Section 1 - Packages
## os ★★
## zipfile
## numpy ★
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

#### tensorflow.keras.callbacks
Callback
TensorBoard
ModelCheckpoint
EarlyStopping
LearningRateScheduler

#### tensorflow.keras.datasets
mnist

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

### google colab
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
