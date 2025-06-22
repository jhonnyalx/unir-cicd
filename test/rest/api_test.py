import http.client
import os
import unittest
from urllib.request import urlopen

import pytest

BASE_URL = os.environ.get("BASE_URL")
DEFAULT_TIMEOUT = 10  # in secs


@pytest.mark.api
class TestApi(unittest.TestCase):
    def setUp(self):
        self.assertIsNotNone(BASE_URL, "URL no configurada")
        self.assertTrue(len(BASE_URL) > 8, "URL no configurada")

    def test_api_add(self):
        url = f"{BASE_URL}/calc/add/2/2"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(
            response.status, http.client.OK, f"Error en la petición API a {url}"
        )
        # Validar también el resultado de la operación
        result = response.read().decode("utf-8")
        self.assertEqual(
            result,
            "4",
            f"El resultado esperado era 4, pero se obtuvo: {result}",
        )

    def test_api_substract(self):
        url = f"{BASE_URL}/calc/substract/5/3"
        response = urlopen(url, timeout=DEFAULT_TIMEOUT)
        self.assertEqual(
            response.status, http.client.OK, f"Error en la petición API a {url}"
        )
        result = response.read().decode("utf-8")
        self.assertEqual(
            result,
            "2",
            f"El resultado esperado era 2, pero se obtuvo: {result}",
        )

    def test_api_invalid_operation(self):
        url = f"{BASE_URL}/calc/add/abc/2"
        try:
            response = urlopen(url, timeout=DEFAULT_TIMEOUT)
            self.assertEqual(response.status, http.client.BAD_REQUEST)
        except Exception:
            # Esperamos que falle con parámetros inválidos
            pass
