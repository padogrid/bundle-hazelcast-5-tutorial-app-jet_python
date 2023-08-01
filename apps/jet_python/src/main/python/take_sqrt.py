import numpy as np
import debugpy

is_debug_initialized = False

def transform_list(input_list):
    # Listen on the default debugger port 5678 on localhost
    global is_debug_initialized
    if is_debug_initialized == False:
        debugpy.listen(("localhost", 5678))
        is_debug_initialized = True

    num_list = [float(it) for it in input_list]
    sqrt_list = np.sqrt(num_list)
    return ["sqrt(%d) = %.2f" % (x, y) for (x, y) in zip(num_list, sqrt_list)]