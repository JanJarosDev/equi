package com.jjdev.equi.core.base.domain

import java.io.IOException

class NetworkException(val code: StatusCode) : IOException()